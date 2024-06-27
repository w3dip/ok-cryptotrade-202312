package ru.otus.otuskotlin.crypto.trade.core.permissions

import ru.otus.otuskotlin.crypto.trade.auth.checkPermitted
import ru.otus.otuskotlin.crypto.trade.auth.resolveRelationsTo
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.helpers.accessViolation
import ru.otus.otuskotlin.crypto.trade.common.helpers.fail
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.chain
import ru.otus.otuskotlin.crypto.trade.cor.worker

fun CorChainDsl<OrderContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == OrderState.RUNNING }
    worker("Вычисление отношения заявки к принципалу") {
        orderRepoRead.principalRelations = orderRepoRead.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к заявке") {
        permitted = checkPermitted(command, orderRepoRead.principalRelations, permissionsChain)
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(
                accessViolation(
                    principal = principal,
                    operation = command,
                    orderId = orderRepoRead.id,
                )
            )
        }
    }
}
