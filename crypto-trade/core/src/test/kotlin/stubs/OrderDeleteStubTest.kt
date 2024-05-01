package ru.otus.otuskotlin.crypto.trade.core.stubs

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.stubs.OrderStubs
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderDeleteStubTest {

    private val processor = OrderProcessor()
    val id = OrderId("001")

    @Test
    fun delete() = runTest {

        val ctx = OrderContext(
            command = OrderCommand.DELETE,
            state = OrderState.NONE,
            workMode = OrderWorkMode.STUB,
            stubCase = OrderStubs.SUCCESS,
            orderRequest = Order(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = OrderStub.get()
        assertEquals(stub.id, ctx.orderResponse.id)
        assertEquals(stub.secCode, ctx.orderResponse.secCode)
        assertEquals(stub.agreementNumber, ctx.orderResponse.agreementNumber)
        assertEquals(stub.operationType, ctx.orderResponse.operationType)
        assertEquals(stub.quantity, ctx.orderResponse.quantity)
        assertEquals(stub.price, ctx.orderResponse.price)
    }

    @Test
    fun badId() = runTest {
        val ctx = OrderContext(
            command = OrderCommand.DELETE,
            state = OrderState.NONE,
            workMode = OrderWorkMode.STUB,
            stubCase = OrderStubs.BAD_ID,
            orderRequest = Order(),
        )
        processor.exec(ctx)
        assertEquals(Order(), ctx.orderResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = OrderContext(
            command = OrderCommand.DELETE,
            state = OrderState.NONE,
            workMode = OrderWorkMode.STUB,
            stubCase = OrderStubs.DB_ERROR,
            orderRequest = Order(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Order(), ctx.orderResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = OrderContext(
            command = OrderCommand.DELETE,
            state = OrderState.NONE,
            workMode = OrderWorkMode.STUB,
            stubCase = OrderStubs.BAD_SEC_CODE,
            orderRequest = Order(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Order(), ctx.orderResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
