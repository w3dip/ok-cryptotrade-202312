package ru.otus.otuskotlin.crypto.trade.core.validation

import CorSettings
import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.repo.common.OrderRepoInitialized
import ru.otus.otuskotlin.crypto.trade.repo.inmemory.OrderRepoInMemory
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub

abstract class BaseBizValidationTest {
    protected abstract val command: OrderCommand
    private val repo = OrderRepoInitialized(
        repo = OrderRepoInMemory(),
        initObjects = listOf(
            OrderStub.get(),
        ),
    )
    private val settings by lazy { CorSettings(repoTest = repo) }
    protected val processor by lazy { OrderProcessor(settings) }
}
