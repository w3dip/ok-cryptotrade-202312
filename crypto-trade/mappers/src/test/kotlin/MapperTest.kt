package ru.otus.otuskotlin.crypto.trade.mappers

import org.junit.Test
import ru.otus.otuskotlin.crypto.trade.api.v1.models.*
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderSide
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.models.OrderSide.BUY
import ru.otus.otuskotlin.crypto.trade.common.stubs.OrderStubs
import java.math.BigDecimal
import kotlin.test.assertEquals

class MapperTest {

    @Test
    fun fromTransport() {
        val req = OrderCreateRequest(
            debug = OrderDebug(
                mode = OrderRequestDebugMode.STUB,
                stub = OrderRequestDebugStubs.SUCCESS,
            ),
            order = OrderCreateObject(
                secCode = "BTC",
                agreementNumber = "A001",
                quantity = BigDecimal.valueOf(5),
                price = BigDecimal.valueOf(65000),
                operationType = OrderSide.BUY,
            ),
        )

        val context = OrderContext()
        context.fromTransport(req)

        assertEquals(OrderStubs.SUCCESS, context.stubCase)
        assertEquals(OrderWorkMode.STUB, context.workMode)
        assertEquals("BTC", context.orderRequest.secCode)
        assertEquals("A001", context.orderRequest.agreementNumber)
        assertEquals(BigDecimal.valueOf(5), context.orderRequest.quantity)
        assertEquals(BigDecimal.valueOf(65000), context.orderRequest.price)
        assertEquals(BUY, context.orderRequest.operationType)
    }

    @Test
    fun toTransport() {
        val context = OrderContext(
            requestId = OrderRequestId("1234"),
            command = OrderCommand.CREATE,
            orderResponse = Order(
                secCode = "BTC",
                agreementNumber = "A001",
                quantity = BigDecimal.valueOf(5),
                price = BigDecimal.valueOf(65000),
                operationType = BUY,
            ),
            errors = mutableListOf(
                OrderError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = OrderState.RUNNING,
        )

        val req = context.toTransportOrder() as OrderCreateResponse

        assertEquals("BTC", req.order?.secCode)
        assertEquals("A001", req.order?.agreementNumber)
        assertEquals(BigDecimal.valueOf(5), req.order?.quantity)
        assertEquals(BigDecimal.valueOf(65000), req.order?.price)
        assertEquals(OrderSide.BUY, req.order?.operationType)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
