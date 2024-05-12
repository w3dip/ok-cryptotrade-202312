package ru.otus.otuskotlin.crypto.trade.core.stubs

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.stubs.OrderStubs
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderCreateStubTest {

    private val processor = OrderProcessor()
    val id = OrderId("001")
    val secCode = "BTC"
    val agreementNumber = "A001"
    val orderSide = OrderSide.BUY
    val quantity = BigDecimal.valueOf(5)
    val price = BigDecimal.valueOf(45000)

    @Test
    fun create() = runTest {
        val ctx = OrderContext(
            command = OrderCommand.CREATE,
            state = OrderState.NONE,
            workMode = OrderWorkMode.STUB,
            stubCase = OrderStubs.SUCCESS,
            orderRequest = Order(
                id = id,
                secCode = secCode,
                agreementNumber = agreementNumber,
                operationType = orderSide,
                quantity = quantity,
                price = price
            ),
        )
        processor.exec(ctx)
        assertEquals(OrderStub.get().id, ctx.orderResponse.id)
        assertEquals(secCode, ctx.orderResponse.secCode)
        assertEquals(agreementNumber, ctx.orderResponse.agreementNumber)
        assertEquals(orderSide, ctx.orderResponse.operationType)
        assertEquals(quantity, ctx.orderResponse.quantity)
        assertEquals(price, ctx.orderResponse.price)
    }

    @Test
    fun badSecCode() = runTest {
        val ctx = OrderContext(
            command = OrderCommand.CREATE,
            state = OrderState.NONE,
            workMode = OrderWorkMode.STUB,
            stubCase = OrderStubs.BAD_SEC_CODE,
            orderRequest = Order(
                id = id,
                secCode = "",
                agreementNumber = agreementNumber,
                operationType = orderSide,
                quantity = quantity,
                price = price
            ),
        )
        processor.exec(ctx)
        assertEquals(Order(), ctx.orderResponse)
        assertEquals("secCode", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badAgreementNumber() = runTest {
        val ctx = OrderContext(
            command = OrderCommand.CREATE,
            state = OrderState.NONE,
            workMode = OrderWorkMode.STUB,
            stubCase = OrderStubs.BAD_AGREEMENT_NUMBER,
            orderRequest = Order(
                id = id,
                secCode = secCode,
                agreementNumber = "",
                operationType = orderSide,
                quantity = quantity,
                price = price
            ),
        )
        processor.exec(ctx)
        assertEquals(Order(), ctx.orderResponse)
        assertEquals("agreementNumber", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun quantityIsZero() = runTest {
        val ctx = OrderContext(
            command = OrderCommand.CREATE,
            state = OrderState.NONE,
            workMode = OrderWorkMode.STUB,
            stubCase = OrderStubs.BAD_QUANTITY,
            orderRequest = Order(
                id = id,
                secCode = secCode,
                agreementNumber = agreementNumber,
                operationType = orderSide,
                quantity = BigDecimal.ZERO,
                price = price
            ),
        )
        processor.exec(ctx)
        assertEquals(Order(), ctx.orderResponse)
        assertEquals("quantity", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun quantityIsNegative() = runTest {
        val ctx = OrderContext(
            command = OrderCommand.CREATE,
            state = OrderState.NONE,
            workMode = OrderWorkMode.STUB,
            stubCase = OrderStubs.BAD_QUANTITY,
            orderRequest = Order(
                id = id,
                secCode = secCode,
                agreementNumber = agreementNumber,
                operationType = orderSide,
                quantity = BigDecimal.valueOf(-100),
                price = price
            ),
        )
        processor.exec(ctx)
        assertEquals(Order(), ctx.orderResponse)
        assertEquals("quantity", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun priceIsZero() = runTest {
        val ctx = OrderContext(
            command = OrderCommand.CREATE,
            state = OrderState.NONE,
            workMode = OrderWorkMode.STUB,
            stubCase = OrderStubs.BAD_PRICE,
            orderRequest = Order(
                id = id,
                secCode = secCode,
                agreementNumber = agreementNumber,
                operationType = orderSide,
                quantity = quantity,
                price = BigDecimal.ZERO
            ),
        )
        processor.exec(ctx)
        assertEquals(Order(), ctx.orderResponse)
        assertEquals("price", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun priceIsNegative() = runTest {
        val ctx = OrderContext(
            command = OrderCommand.CREATE,
            state = OrderState.NONE,
            workMode = OrderWorkMode.STUB,
            stubCase = OrderStubs.BAD_PRICE,
            orderRequest = Order(
                id = id,
                secCode = secCode,
                agreementNumber = agreementNumber,
                operationType = orderSide,
                quantity = quantity,
                price = BigDecimal.valueOf(-100)
            ),
        )
        processor.exec(ctx)
        assertEquals(Order(), ctx.orderResponse)
        assertEquals("price", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = OrderContext(
            command = OrderCommand.CREATE,
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
            command = OrderCommand.CREATE,
            state = OrderState.NONE,
            workMode = OrderWorkMode.STUB,
            stubCase = OrderStubs.BAD_ID,
            orderRequest = Order(
                id = id,
                secCode = secCode,
                agreementNumber = agreementNumber,
                operationType = orderSide,
                quantity = quantity,
                price = price
            ),
        )
        processor.exec(ctx)
        assertEquals(Order(), ctx.orderResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
