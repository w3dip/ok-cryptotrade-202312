package ru.otus.otuskotlin.crypto.trade.stubs

import ru.otus.otuskotlin.crypto.trade.common.models.*
import java.math.BigDecimal

object OrderStub {

    val ORDER_BUY: Order
        get() = Order(
            id = OrderId("001"),
            secCode = "BTC",
            agreementNumber = "A001",
            quantity = BigDecimal.valueOf(1000.00),
            price = BigDecimal.valueOf(100_000.00),
            userId = OrderUserId("user-1"),
            operationType = OrderSide.BUY,
            permissionsClient = mutableSetOf(
                OrderPermissionClient.READ,
                OrderPermissionClient.UPDATE,
                OrderPermissionClient.DELETE,
            ),
            lock = OrderLock("123-234-abc-ABC"),
        )
    val ORDER_SELL = ORDER_BUY.copy(operationType = OrderSide.SELL)

    fun get(): Order = ORDER_BUY.copy()

    fun prepareResult(block: Order.() -> Unit): Order = get().apply(block)

    fun prepareResultList(filter: String, type: OrderSide) = (1..10).map { orderBuy(it.toString(), filter, type) }

    private fun orderBuy(id: String, filter: String, type: OrderSide) =
        order(ORDER_BUY, id = id, filter = filter, type = type)

    private fun orderSell(id: String, filter: String, type: OrderSide) =
        order(ORDER_SELL, id = id, filter = filter, type = type)

    private fun order(base: Order, id: String, filter: String, type: OrderSide) = base.copy(
        id = OrderId(id),
        secCode = "$filter $id",
        operationType = type
    )
}
