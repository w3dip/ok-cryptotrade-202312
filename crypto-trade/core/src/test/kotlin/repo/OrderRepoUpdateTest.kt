package ru.otus.otuskotlin.crypto.trade.core.repo

import CorSettings
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.repo.tests.OrderRepoMock
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub
import ru.otus.otuskotlin.marketplace.biz.addTestPrincipal
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderRepoUpdateTest {

    private val userId = OrderStub.ORDER_BUY.userId
    private val command = OrderCommand.UPDATE
    private val initOrder = Order(
        id = OrderId("123"),
        secCode = "abc",
        agreementNumber = "abc",
        quantity = BigDecimal.valueOf(1100),
        price = BigDecimal.valueOf(120000),
        operationType = OrderSide.BUY,
        userId = userId,
        lock = OrderLock("123-234-abc-ABC"),
    )
    private val repo = OrderRepoMock(
        invokeReadOrder = {
            DbOrderResponseOk(
                data = initOrder,
            )
        },
        invokeUpdateOrder = {
            DbOrderResponseOk(
                data = Order(
                    id = OrderId("123"),
                    secCode = "xyz",
                    agreementNumber = "xyz",
                    quantity = BigDecimal.valueOf(1500),
                    price = BigDecimal.valueOf(100),
                    operationType = OrderSide.BUY,
                    userId = userId,
                    lock = OrderLock("123-234-abc-ABC"),
                )
            )
        }
    )
    private val settings = CorSettings(repoTest = repo)
    private val processor = OrderProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val orderToUpdate = Order(
            id = OrderId("123"),
            secCode = "xyz",
            agreementNumber = "xyz",
            quantity = BigDecimal.valueOf(1500),
            price = BigDecimal.valueOf(100),
            operationType = OrderSide.BUY,
            userId = userId,
            lock = OrderLock("123-234-abc-ABC"),
        )
        val ctx = OrderContext(
            command = command,
            state = OrderState.NONE,
            workMode = OrderWorkMode.TEST,
            orderRequest = orderToUpdate,
        )
        ctx.addTestPrincipal()
        processor.exec(ctx)
        assertEquals(OrderState.FINISHING, ctx.state)
        assertEquals(orderToUpdate.id, ctx.orderResponse.id)
        assertEquals(orderToUpdate.secCode, ctx.orderResponse.secCode)
        assertEquals(orderToUpdate.agreementNumber, ctx.orderResponse.agreementNumber)
        assertEquals(orderToUpdate.quantity, ctx.orderResponse.quantity)
        assertEquals(orderToUpdate.price, ctx.orderResponse.price)
        assertEquals(orderToUpdate.operationType, ctx.orderResponse.operationType)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
