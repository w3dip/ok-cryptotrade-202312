package ru.otus.otuskotlin.crypto.trade.core.stubs

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.stubs.OrderStubs
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class OrderSearchStubTest {

    private val processor = OrderProcessor()
    val filter = OrderFilter(searchString = "BTC")

    @Test
    fun read() = runTest {

        val ctx = OrderContext(
            command = OrderCommand.SEARCH,
            state = OrderState.NONE,
            workMode = OrderWorkMode.STUB,
            stubCase = OrderStubs.SUCCESS,
            orderFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.ordersResponse.size > 1)
        val first = ctx.ordersResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.secCode.contains(filter.searchString))
        with(OrderStub.get()) {
            assertEquals(operationType, first.operationType)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = OrderContext(
            command = OrderCommand.SEARCH,
            state = OrderState.NONE,
            workMode = OrderWorkMode.STUB,
            stubCase = OrderStubs.BAD_ID,
            orderFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Order(), ctx.orderResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = OrderContext(
            command = OrderCommand.SEARCH,
            state = OrderState.NONE,
            workMode = OrderWorkMode.STUB,
            stubCase = OrderStubs.DB_ERROR,
            orderFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Order(), ctx.orderResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = OrderContext(
            command = OrderCommand.SEARCH,
            state = OrderState.NONE,
            workMode = OrderWorkMode.STUB,
            stubCase = OrderStubs.BAD_SEC_CODE,
            orderFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Order(), ctx.orderResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
