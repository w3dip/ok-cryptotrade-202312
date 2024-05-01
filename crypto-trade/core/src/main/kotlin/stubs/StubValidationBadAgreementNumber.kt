package ru.otus.otuskotlin.crypto.trade.core.stubs

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.helpers.fail
import ru.otus.otuskotlin.crypto.trade.common.models.OrderError
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.common.stubs.OrderStubs
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker

fun CorChainDsl<OrderContext>.stubValidationBadAgreementNumber(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для номера счета
    """.trimIndent()

    on { stubCase == OrderStubs.BAD_AGREEMENT_NUMBER && state == OrderState.RUNNING }
    handle {
        fail(
            OrderError(
                group = "validation",
                code = "validation-agreement-number",
                field = "agreementNumber",
                message = "Wrong agreementNumber field"
            )
        )
    }
}
