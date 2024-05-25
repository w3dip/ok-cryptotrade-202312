package ru.otus.otuskotlin.crypto.trade.repo.tests

import ru.otus.otuskotlin.crypto.trade.common.models.Order
import ru.otus.otuskotlin.crypto.trade.common.repo.*

class OrderRepositoryMock(
    private val invokeCreateOrder: (DbOrderRequest) -> IDbOrderResponse = { DEFAULT_ORDER_SUCCESS_EMPTY_MOCK },
    private val invokeReadOrder: (DbOrderIdRequest) -> IDbOrderResponse = { DEFAULT_ORDER_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateOrder: (DbOrderRequest) -> IDbOrderResponse = { DEFAULT_ORDER_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteOrder: (DbOrderIdRequest) -> IDbOrderResponse = { DEFAULT_ORDER_SUCCESS_EMPTY_MOCK },
    private val invokeSearchOrder: (DbOrderFilterRequest) -> IDbOrdersResponse = { DEFAULT_ORDERS_SUCCESS_EMPTY_MOCK },
) : IRepoOrder {
    override suspend fun createOrder(rq: DbOrderRequest): IDbOrderResponse {
        return invokeCreateOrder(rq)
    }

    override suspend fun readOrder(rq: DbOrderIdRequest): IDbOrderResponse {
        return invokeReadOrder(rq)
    }

    override suspend fun updateOrder(rq: DbOrderRequest): IDbOrderResponse {
        return invokeUpdateOrder(rq)
    }

    override suspend fun deleteOrder(rq: DbOrderIdRequest): IDbOrderResponse {
        return invokeDeleteOrder(rq)
    }

    override suspend fun searchOrder(rq: DbOrderFilterRequest): IDbOrdersResponse {
        return invokeSearchOrder(rq)
    }

    companion object {
        val DEFAULT_ORDER_SUCCESS_EMPTY_MOCK = DbOrderResponseOk(Order())
        val DEFAULT_ORDERS_SUCCESS_EMPTY_MOCK = DbOrdersResponseOk(emptyList())
    }
}
