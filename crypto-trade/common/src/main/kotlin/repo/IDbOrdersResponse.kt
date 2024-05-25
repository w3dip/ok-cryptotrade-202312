package ru.otus.otuskotlin.crypto.trade.common.repo

import ru.otus.otuskotlin.crypto.trade.common.models.Order
import ru.otus.otuskotlin.crypto.trade.common.models.OrderError

sealed interface IDbOrdersResponse : IDbResponse<List<Order>>

data class DbOrdersResponseOk(
    val data: List<Order>
) : IDbOrdersResponse

@Suppress("unused")
data class DbOrdersResponseErr(
    val errors: List<OrderError> = emptyList()
) : IDbOrdersResponse {
    constructor(err: OrderError) : this(listOf(err))
}
