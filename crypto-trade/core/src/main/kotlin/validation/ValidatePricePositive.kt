package ru.otus.otuskotlin.crypto.trade.core.validation

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.helpers.errorValidation
import ru.otus.otuskotlin.crypto.trade.common.helpers.fail
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker
import java.math.BigDecimal

fun CorChainDsl<OrderContext>.validatePricePositive(title: String) = worker {
    this.title = title
    on { orderValidating.price.compareTo(BigDecimal.ZERO) <= 0 }
    handle {
        fail(
            errorValidation(
                field = "price",
                violationCode = "is not positive",
                description = "field must be positive"
            )
        )
    }
}
