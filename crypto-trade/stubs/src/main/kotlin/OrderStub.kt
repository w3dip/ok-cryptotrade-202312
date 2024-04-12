package ru.otus.otuskotlin.crypto.trade.stubs

import randomUUID
import ru.otus.otuskotlin.crypto.trade.common.models.*
import java.math.BigDecimal

object OrderStub {
    val ORDER_BUY: Order
        get() = Order(
            id = OrderId(randomUUID()),
            secCode = "BTC",
            agreementNumber = "001",
            quantity = BigDecimal.valueOf(5),
            price = BigDecimal.valueOf(65000),
            userId = OrderUserId(randomUUID()),
            operationType = OrderSide.BUY,
            permissionsClient = mutableSetOf(
                OrderPermissionClient.READ,
                OrderPermissionClient.UPDATE,
                OrderPermissionClient.DELETE,
            )
        )
    val ORDER_SELL = ORDER_BUY.copy(operationType = OrderSide.SELL)

    fun get(): Order = ORDER_BUY.copy()

    fun prepareResult(block: Order.() -> Unit): Order = get().apply(block)

    fun prepareResultList() = listOf(
        (1..10).let { orderBuy(randomUUID()) }
    )

    private fun orderBuy(id: String) =
        order(ORDER_BUY, id = id)

    private fun orderSell(id: String) =
        order(ORDER_SELL, id = id)

    private fun order(base: Order, id: String) = base.copy(
        id = OrderId(id)
    )
}
