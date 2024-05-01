package ru.otus.otuskotlin.crypto.trade.core.stubs

import CorSettings
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.common.stubs.OrderStubs
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker
import ru.otus.otuskotlin.crypto.trade.logging.common.LogLevel
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub

fun CorChainDsl<OrderContext>.stubDeleteSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для удаления заявки
    """.trimIndent()
    on { stubCase == OrderStubs.SUCCESS && state == OrderState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubDeleteSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = OrderState.FINISHING
            val stub = OrderStub.prepareResult {
                orderRequest.secCode.takeIf { it.isNotBlank() }?.also { this.secCode = it }
            }
            orderResponse = stub
        }
    }
}
