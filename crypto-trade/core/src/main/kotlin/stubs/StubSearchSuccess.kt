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

fun CorChainDsl<OrderContext>.stubSearchSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для поиска заявок
    """.trimIndent()
    on { stubCase == OrderStubs.SUCCESS && state == OrderState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubSearchSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = OrderState.FINISHING
            ordersResponse.addAll(OrderStub.prepareResultList(orderFilterRequest.searchString, OrderSide.BUY))
        }
    }
}
