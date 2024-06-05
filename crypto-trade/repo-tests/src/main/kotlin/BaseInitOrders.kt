package ru.otus.otuskotlin.crypto.trade.repo.tests

import ru.otus.otuskotlin.crypto.trade.common.models.*
import java.math.BigDecimal

abstract class BaseInitOrders(private val op: String) : InitObjects<Order> {
    open val lockOld: OrderLock = OrderLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: OrderLock = OrderLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        userId: OrderUserId = OrderUserId("user-123"),
        operationType: OrderSide = OrderSide.BUY,
        lock: OrderLock = lockOld,
    ) = Order(
        id = OrderId("order-repo-$op-$suf"),
        secCode = "$suf stub sec code",
        agreementNumber = "$suf stub agreement number",
        quantity = BigDecimal.valueOf(100),
        price = BigDecimal.valueOf(10000),
        userId = userId,
        operationType = operationType,
        lock = lock,
    )
}
