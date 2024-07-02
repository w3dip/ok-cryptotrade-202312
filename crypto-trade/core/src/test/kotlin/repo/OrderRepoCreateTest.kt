package ru.otus.otuskotlin.crypto.trade.core.repo

import CorSettings
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.repo.tests.OrderRepoMock
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub.ORDER_BUY
import ru.otus.otuskotlin.marketplace.biz.addTestPrincipal
import java.math.BigDecimal
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class OrderRepoCreateTest {

    private val userId = ORDER_BUY.userId
    private val command = OrderCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = OrderRepoMock(
        invokeCreateOrder = {
            DbOrderResponseOk(
                data = Order(
                    id = OrderId(uuid),
                    secCode = it.order.secCode,
                    agreementNumber = it.order.agreementNumber,
                    quantity = it.order.quantity,
                    price = it.order.price,
                    userId = userId,
                    operationType = it.order.operationType
                )
            )
        }
    )
    private val settings = CorSettings(
        repoTest = repo
    )
    private val processor = OrderProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = OrderContext(
            command = command,
            state = OrderState.NONE,
            workMode = OrderWorkMode.TEST,
            orderRequest = Order(
                secCode = "abc",
                agreementNumber = "abc",
                quantity = BigDecimal.valueOf(1100),
                price = BigDecimal.valueOf(120000),
                operationType = OrderSide.BUY
            ),
        )
        ctx.addTestPrincipal()
        processor.exec(ctx)
        assertEquals(OrderState.FINISHING, ctx.state)
        assertNotEquals(OrderId.NONE, ctx.orderResponse.id)
        assertEquals("abc", ctx.orderResponse.secCode)
        assertEquals("abc", ctx.orderResponse.agreementNumber)
        assertEquals(BigDecimal.valueOf(1100), ctx.orderResponse.quantity)
        assertEquals(BigDecimal.valueOf(120000), ctx.orderResponse.price)
        assertEquals(OrderSide.BUY, ctx.orderResponse.operationType)
    }

    @Test
    fun randomUUID() = runTest {
        println(UUID.randomUUID().toString())
        println(UUID.randomUUID().toString())
    }
}
