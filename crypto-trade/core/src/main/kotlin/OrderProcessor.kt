package ru.otus.otuskotlin.crypto.trade.core

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub

@Suppress("unused", "RedundantSuspendModifier")
class OrderProcessor() {
    suspend fun exec(ctx: OrderContext) {
        ctx.orderResponse = OrderStub.get()
        ctx.ordersResponse = OrderStub.prepareResultList().toMutableList()
        ctx.state = OrderState.RUNNING
    }
}
