package ru.otus.otuskotlin.crypto.trade.repo.tests

import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderRequest
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.crypto.trade.repo.common.OrderRepoInitialized
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

abstract class OrderRepoCreateTest {
    abstract val repo: OrderRepoInitialized
    protected open val lockNew = OrderLock("20000000-0000-0000-0000-000000000002")

    private val createObj = Order(
        secCode = "test sec code",
        agreementNumber = "test agreement number",
        userId = OrderUserId("user-123"),
        quantity = BigDecimal.valueOf(200),
        price = BigDecimal.valueOf(30000),
        operationType = OrderSide.BUY,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createOrder(DbOrderRequest(createObj))
        val expected = createObj
        assertIs<DbOrderResponseOk>(result)
        assertNotEquals(OrderId.NONE, result.data.id)
        assertEquals(lockNew, result.data.lock)
        assertEquals(expected.secCode, result.data.secCode)
        assertEquals(expected.agreementNumber, result.data.agreementNumber)
        assertEquals(expected.operationType, result.data.operationType)
        assertNotEquals(OrderId.NONE, result.data.id)
    }

    companion object : BaseInitOrders("create") {
        override val initObjects: List<Order> = emptyList()
    }
}
