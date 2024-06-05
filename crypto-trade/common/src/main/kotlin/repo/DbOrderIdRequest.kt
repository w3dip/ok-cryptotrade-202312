package ru.otus.otuskotlin.crypto.trade.common.repo

import ru.otus.otuskotlin.crypto.trade.common.models.Order
import ru.otus.otuskotlin.crypto.trade.common.models.OrderId
import ru.otus.otuskotlin.crypto.trade.common.models.OrderLock

data class DbOrderIdRequest(
    val id: OrderId,
    val lock: OrderLock = OrderLock.NONE,
) {
    constructor(order: Order) : this(order.id, order.lock)
}
