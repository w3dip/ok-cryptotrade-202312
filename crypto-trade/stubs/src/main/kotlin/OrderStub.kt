package ru.otus.otuskotlin.crypto.trade.stubs

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import randomUUID
import ru.otus.otuskotlin.crypto.trade.common.models.*
import java.math.BigDecimal
import kotlin.random.Random

object OrderStub {

    val ORDER_BUY: Order
        get() = Order(
            id = OrderId(randomUUID()),
            secCode = randomAlphabetic(3).uppercase(),
            agreementNumber = randomAlphanumeric(5).uppercase(),
            quantity = BigDecimal.valueOf(Random.nextDouble(1000.00)),
            price = BigDecimal.valueOf(Random.nextDouble(100_000.00)),
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

    fun prepareResultList() = (1..10).map { orderBuy() }

    private fun orderBuy() = ORDER_BUY.copy()

    private fun orderSell() = ORDER_SELL.copy()
}
