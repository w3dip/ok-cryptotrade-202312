package ru.otus.otuskotlin.crypto.trade.core

import CorSettings
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderId
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub

class OrderProcessor(val corSettings: CorSettings) {
    suspend fun exec(ctx: OrderContext) {
        ctx.orderResponse = OrderStub.prepareResult {
            if (ctx.orderRequest.id != OrderId.NONE) {
                this.id = ctx.orderRequest.id
            }
        }
        ctx.ordersResponse = OrderStub.prepareResultList().toMutableList()
        ctx.state = OrderState.RUNNING
    }
}
