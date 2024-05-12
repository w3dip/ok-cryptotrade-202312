package ru.otus.otuskotlin.crypto.trade.core.stubs

import CorSettings
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderSide
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.common.stubs.OrderStubs
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker
import ru.otus.otuskotlin.crypto.trade.logging.common.LogLevel
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub
import java.math.BigDecimal

fun CorChainDsl<OrderContext>.stubCreateSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для создания заявки
    """.trimIndent()
    on { stubCase == OrderStubs.SUCCESS && state == OrderState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubCreateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = OrderState.FINISHING
            val stub = OrderStub.prepareResult {
                orderRequest.secCode.takeIf { it.isNotBlank() }?.also { this.secCode = it }
                orderRequest.agreementNumber.takeIf { it.isNotBlank() }?.also { this.agreementNumber = it }
                orderRequest.quantity.takeIf { it != BigDecimal.ZERO }?.also { this.quantity = it }
                orderRequest.price.takeIf { it != BigDecimal.ZERO }?.also { this.price = it }
                orderRequest.operationType.takeIf { it != OrderSide.NONE }?.also { this.operationType = it }
            }
            orderResponse = stub
        }
    }
}
