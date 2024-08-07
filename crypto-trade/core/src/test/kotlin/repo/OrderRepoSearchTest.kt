package ru.otus.otuskotlin.marketplace.biz.repo

import CorSettings
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrdersResponseOk
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.repo.tests.OrderRepoMock
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub
import ru.otus.otuskotlin.marketplace.biz.addTestPrincipal
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderRepoSearchTest {

    private val userId = OrderStub.ORDER_BUY.userId
    private val command = OrderCommand.SEARCH
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
        invokeSearchOrder = {
            DbOrdersResponseOk(
                data = listOf(initOrder),
            )
        }
    )
    private val settings = CorSettings(repoTest = repo)
    private val processor = OrderProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = OrderContext(
            command = command,
            state = OrderState.NONE,
            workMode = OrderWorkMode.TEST,
            orderFilterRequest = OrderFilter(
                searchString = "abc",
                operationType = OrderSide.BUY
            ),
        )
        ctx.addTestPrincipal()
        processor.exec(ctx)
        assertEquals(OrderState.FINISHING, ctx.state)
        assertEquals(1, ctx.ordersResponse.size)
    }
}
