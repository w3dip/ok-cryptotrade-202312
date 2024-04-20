package ru.otus.otuskotlin.crypto.trade.common.helpers

import ru.otus.otuskotlin.crypto.trade.common.models.OrderError

fun Throwable.asOrderError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = OrderError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
