package ru.otus.otuskotlin.crypto.trade.app

import CorSettings
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor

data class AppSettings(
    val appUrls: List<String> = emptyList(),
    val corSettings: CorSettings = CorSettings(),
    val processor: OrderProcessor = OrderProcessor(corSettings),
)
