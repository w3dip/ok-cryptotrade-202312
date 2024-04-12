package ru.otus.otuskotlin.crypto.trade.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.stubs.OrderStubs

data class OrderContext(
    var command: OrderCommand = OrderCommand.NONE,
    var state: OrderState = OrderState.NONE,
    val errors: MutableList<OrderError> = mutableListOf(),

    var workMode: OrderWorkMode = OrderWorkMode.PROD,
    var stubCase: OrderStubs = OrderStubs.NONE,

    var requestId: OrderRequestId = OrderRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var orderRequest: Order = Order(),
    var orderFilterRequest: OrderFilter = OrderFilter(),

    var orderResponse: Order = Order(),
    var ordersResponse: MutableList<Order> = mutableListOf()
)