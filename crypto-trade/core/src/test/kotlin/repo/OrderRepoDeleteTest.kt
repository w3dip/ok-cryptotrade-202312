package ru.otus.otuskotlin.crypto.trade.core.repo

import CorSettings
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseErr
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.repo.tests.OrderRepoMock
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrderRepoDeleteTest {

    private val userId = OrderUserId("321")
    private val command = OrderCommand.DELETE
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
        invokeDeleteOrder = {
            if (it.id == initOrder.id)
                DbOrderResponseOk(
                    data = initOrder
                )
            else DbOrderResponseErr()
        }
    )
    private val settings by lazy {
        CorSettings(
            repoTest = repo
        )
    }
    private val processor = OrderProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val orderToUpdate = Order(
            id = OrderId("123"),
            lock = OrderLock("123-234-abc-ABC"),
        )
        val ctx = OrderContext(
            command = command,
            state = OrderState.NONE,
            workMode = OrderWorkMode.TEST,
            orderRequest = orderToUpdate,
        )
        processor.exec(ctx)
        assertEquals(OrderState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initOrder.id, ctx.orderResponse.id)
        assertEquals(initOrder.secCode, ctx.orderResponse.secCode)
        assertEquals(initOrder.agreementNumber, ctx.orderResponse.agreementNumber)
        assertEquals(initOrder.quantity, ctx.orderResponse.quantity)
        assertEquals(initOrder.price, ctx.orderResponse.price)
        assertEquals(initOrder.operationType, ctx.orderResponse.operationType)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
