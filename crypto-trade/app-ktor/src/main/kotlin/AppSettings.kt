package ru.otus.otuskotlin.crypto.trade.app

import CorSettings
import ru.otus.otuskotlin.crypto.trade.app.common.CommonAppSettings
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor

data class AppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: CorSettings = CorSettings(),
    override val processor: OrderProcessor = OrderProcessor(corSettings),
) : CommonAppSettings
