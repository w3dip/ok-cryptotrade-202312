package ru.otus.otuskotlin.crypto.trade.core.validation

import CorSettings
import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor

abstract class BaseBizValidationTest {
    protected abstract val command: OrderCommand
    private val settings by lazy { CorSettings() }
    protected val processor by lazy { OrderProcessor(settings) }
}
