package ru.otus.otuskotlin.crypto.trade.core.validation

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.helpers.errorValidation
import ru.otus.otuskotlin.crypto.trade.common.helpers.fail
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker
import java.math.BigDecimal

fun CorChainDsl<OrderContext>.validateQuantityPositive(title: String) = worker {
    this.title = title
    on { orderValidating.quantity.compareTo(BigDecimal.ZERO) <= 0 }
    handle {
        fail(
            errorValidation(
                field = "quantity",
                violationCode = "is not positive",
                description = "field must be positive"
            )
        )
    }
}
