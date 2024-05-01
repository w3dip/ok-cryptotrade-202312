package ru.otus.otuskotlin.crypto.trade.core.stubs

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.helpers.fail
import ru.otus.otuskotlin.crypto.trade.common.models.OrderError
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.common.stubs.OrderStubs
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker

fun CorChainDsl<OrderContext>.stubValidationBadSecCode(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для кода актива
    """.trimIndent()
    on { stubCase == OrderStubs.BAD_SEC_CODE && state == OrderState.RUNNING }
    handle {
        fail(
            OrderError(
                group = "validation",
                code = "validation-secCode",
                field = "secCode",
                message = "Wrong secCode field"
            )
        )
    }
}
