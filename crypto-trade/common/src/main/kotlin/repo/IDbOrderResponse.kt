package ru.otus.otuskotlin.crypto.trade.common.repo

import ru.otus.otuskotlin.crypto.trade.common.models.Order
import ru.otus.otuskotlin.crypto.trade.common.models.OrderError

sealed interface IDbOrderResponse : IDbResponse<Order>

data class DbOrderResponseOk(
    val data: Order
) : IDbOrderResponse

data class DbOrderResponseErr(
    val errors: List<OrderError> = emptyList()
) : IDbOrderResponse {
    constructor(err: OrderError) : this(listOf(err))
}

data class DbOrderResponseErrWithData(
    val data: Order,
    val errors: List<OrderError> = emptyList()
) : IDbOrderResponse {
    constructor(order: Order, err: OrderError) : this(order, listOf(err))
}
