package ru.otus.otuskotlin.crypto.trade.app.common

import CorSettings
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor

interface CommonAppSettings {
    val processor: OrderProcessor
    val corSettings: CorSettings
}
