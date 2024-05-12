package ru.otus.otuskotlin.crypto.trade.core.validation

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.helpers.errorValidation
import ru.otus.otuskotlin.crypto.trade.common.helpers.fail
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker

fun CorChainDsl<OrderContext>.validateSecCodeNotEmpty(title: String) = worker {
    this.title = title
    on { orderValidating.secCode.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "secCode",
                violationCode = "empty",
                description = "field must be not empty"
            )
        )
    }
}
