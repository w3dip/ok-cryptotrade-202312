package ru.otus.otuskotlin.crypto.trade.common.models

data class OrderError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)
