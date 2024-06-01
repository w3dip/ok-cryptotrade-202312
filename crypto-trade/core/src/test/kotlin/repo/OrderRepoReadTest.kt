package ru.otus.otuskotlin.crypto.trade.core.repo

import CorSettings
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.repo.tests.OrderRepoMock
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderRepoReadTest {

    private val userId = OrderUserId("321")
    private val command = OrderCommand.READ
    private val initOrder = Order(
        id = OrderId("123"),
        secCode = "abc",
        agreementNumber = "abc",
        quantity = BigDecimal.valueOf(1100),
        price = BigDecimal.valueOf(120000),
        operationType = OrderSide.BUY,
        userId = userId,
    )
    private val repo = OrderRepoMock(
        invokeReadOrder = {
            DbOrderResponseOk(
                data = initOrder,
            )
        }
    )
    private val settings = CorSettings(repoTest = repo)
    private val processor = OrderProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = OrderContext(
            command = command,
            state = OrderState.NONE,
            workMode = OrderWorkMode.TEST,
            orderRequest = Order(
                id = OrderId("123"),
            ),
        )
        processor.exec(ctx)
        assertEquals(OrderState.FINISHING, ctx.state)
        assertEquals(initOrder.id, ctx.orderResponse.id)
        assertEquals(initOrder.secCode, ctx.orderResponse.secCode)
        assertEquals(initOrder.agreementNumber, ctx.orderResponse.agreementNumber)
        assertEquals(initOrder.quantity, ctx.orderResponse.quantity)
        assertEquals(initOrder.price, ctx.orderResponse.price)
        assertEquals(initOrder.operationType, ctx.orderResponse.operationType)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
