package ru.otus.otuskotlin.crypto.trade.core.validation

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.helpers.errorValidation
import ru.otus.otuskotlin.crypto.trade.common.helpers.fail
import ru.otus.otuskotlin.crypto.trade.common.models.OrderLock
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker

fun CorChainDsl<OrderContext>.validateLockProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в OrderId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { orderValidating.lock != OrderLock.NONE && !orderValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = orderValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}
