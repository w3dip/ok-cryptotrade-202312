package ru.otus.otuskotlin.crypto.trade.repo.stubs

import ru.otus.otuskotlin.crypto.trade.common.models.OrderSide
import ru.otus.otuskotlin.crypto.trade.common.repo.*
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub

class OrderRepoStub() : IRepoOrder {
    override suspend fun createOrder(rq: DbOrderRequest): IDbOrderResponse {
        return DbOrderResponseOk(
            data = OrderStub.get(),
        )
    }

    override suspend fun readOrder(rq: DbOrderIdRequest): IDbOrderResponse {
        return DbOrderResponseOk(
            data = OrderStub.get(),
        )
    }

    override suspend fun updateOrder(rq: DbOrderRequest): IDbOrderResponse {
        return DbOrderResponseOk(
            data = OrderStub.get(),
        )
    }

    override suspend fun deleteOrder(rq: DbOrderIdRequest): IDbOrderResponse {
        return DbOrderResponseOk(
            data = OrderStub.get(),
        )
    }

    override suspend fun searchOrder(rq: DbOrderFilterRequest): IDbOrdersResponse {
        return DbOrdersResponseOk(
            data = OrderStub.prepareResultList(filter = "", OrderSide.BUY),
        )
    }
}
