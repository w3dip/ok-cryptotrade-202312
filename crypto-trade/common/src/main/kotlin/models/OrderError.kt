package ru.otus.otuskotlin.crypto.trade.common.models

import ru.otus.otuskotlin.crypto.trade.logging.common.LogLevel

data class OrderError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)
