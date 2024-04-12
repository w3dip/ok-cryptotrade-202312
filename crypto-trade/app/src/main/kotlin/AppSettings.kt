package ru.otus.otuskotlin.crypto.trade.app

import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor

data class AppSettings(
    val appUrls: List<String> = emptyList(),
    val processor: OrderProcessor = OrderProcessor(),
)
