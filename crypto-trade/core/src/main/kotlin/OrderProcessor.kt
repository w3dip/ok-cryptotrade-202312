package ru.otus.otuskotlin.crypto.trade.core

import CorSettings
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand
import ru.otus.otuskotlin.crypto.trade.common.models.OrderId
import ru.otus.otuskotlin.crypto.trade.common.models.OrderLock
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.cor.chain
import ru.otus.otuskotlin.crypto.trade.cor.rootChain
import ru.otus.otuskotlin.crypto.trade.cor.worker
import ru.otus.otuskotlin.crypto.trade.core.general.initStatus
import ru.otus.otuskotlin.crypto.trade.core.general.operation
import ru.otus.otuskotlin.crypto.trade.core.repo.*
import ru.otus.otuskotlin.crypto.trade.core.stubs.*
import ru.otus.otuskotlin.crypto.trade.core.validation.*

class OrderProcessor(
    private val corSettings: CorSettings = CorSettings.NONE
) {
    suspend fun exec(ctx: OrderContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<OrderContext> {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")

        operation("Создание заявки", OrderCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadSecCode("Имитация ошибки валидации кода актива")
                stubValidationBadAgreementNumber("Имитация ошибки валидации номера счета")
                stubValidationBadQuantity("Имитация ошибки валидации количества")
                stubValidationBadPrice("Имитация ошибки валидации цены")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в orderValidating") { orderValidating = orderRequest.deepCopy() }
                worker("Очистка id") { orderValidating.id = OrderId.NONE }
                worker("Очистка кода актива") { orderValidating.secCode = orderValidating.secCode.trim() }
                worker("Очистка номера счета") {
                    orderValidating.agreementNumber = orderValidating.agreementNumber.trim()
                }
                validateSecCodeNotEmpty("Проверка, что код актива не пуст")
                validateSecCodeHasContent("Проверка символов")
                validateAgreementNumberNotEmpty("Проверка, что номер счета не пуст")
                validateAgreementNumberHasContent("Проверка символов")
                validateQuantityPositive("Проверка, что количество актива больше 0")
                validatePricePositive("Проверка, что цена актива больше 0")

                finishOrderValidation("Завершение проверок")
            }
            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание заявки в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Получить заявку", OrderCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в orderValidating") { orderValidating = orderRequest.deepCopy() }
                worker("Очистка id") { orderValidating.id = OrderId(orderValidating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishOrderValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика чтения"
                repoRead("Чтение заявки из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == OrderState.RUNNING }
                    handle { orderRepoDone = orderRepoRead }
                }
            }
            prepareResult("Подготовка ответа")

        }
        operation("Изменить заявку", OrderCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadSecCode("Имитация ошибки валидации кода актива")
                stubValidationBadAgreementNumber("Имитация ошибки валидации номера счета")
                stubValidationBadQuantity("Имитация ошибки валидации количества")
                stubValidationBadPrice("Имитация ошибки валидации цены")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в orderValidating") { orderValidating = orderRequest.deepCopy() }
                worker("Очистка id") { orderValidating.id = OrderId(orderValidating.id.asString().trim()) }
                worker("Очистка lock") { orderValidating.lock = OrderLock(orderValidating.lock.asString().trim()) }
                worker("Очистка кода актива") { orderValidating.secCode = orderValidating.secCode.trim() }
                worker("Очистка номера счета") {
                    orderValidating.agreementNumber = orderValidating.agreementNumber.trim()
                }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                validateSecCodeNotEmpty("Проверка, что код актива не пуст")
                validateSecCodeHasContent("Проверка символов")
                validateAgreementNumberNotEmpty("Проверка, что номер счета не пуст")
                validateAgreementNumberHasContent("Проверка символов")
                validateQuantityPositive("Проверка, что количество актива больше 0")
                validatePricePositive("Проверка, что цена актива больше 0")

                finishOrderValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика сохранения"
                repoRead("Чтение заявки из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareUpdate("Подготовка объекта для обновления")
                repoUpdate("Обновление заявки в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Удалить заявку", OrderCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в orderValidating") {
                    orderValidating = orderRequest.deepCopy()
                }
                worker("Очистка id") { orderValidating.id = OrderId(orderValidating.id.asString().trim()) }
                worker("Очистка lock") { orderValidating.lock = OrderLock(orderValidating.lock.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                finishOrderValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика удаления"
                repoRead("Чтение заявки из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление заявки из БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Поиск заявок", OrderCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в orderFilterValidating") {
                    orderFilterValidating = orderFilterRequest.deepCopy()
                }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishOrderFilterValidation("Успешное завершение процедуры валидации")
            }
            repoSearch("Поиск заявки в БД по фильтру")
            prepareResult("Подготовка ответа")
        }
    }.build()
}
