package ru.otus.otuskotlin.crypto.trade.core.repo

import CorSettings
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.crypto.trade.common.repo.errorNotFound
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.repo.tests.OrderRepoMock
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initOrder = Order(
    id = OrderId("123"),
    secCode = "abc",
    agreementNumber = "abc",
    quantity = BigDecimal.valueOf(1100),
    price = BigDecimal.valueOf(120000),
    operationType = OrderSide.BUY,
)

private val repo = OrderRepoMock(
    invokeReadOrder = {
        if (it.id == initOrder.id) {
            DbOrderResponseOk(
                data = initOrder,
            )
        } else errorNotFound(it.id)
    }
)
private val settings = CorSettings(repoTest = repo)
private val processor = OrderProcessor(settings)

fun repoNotFoundTest(command: OrderCommand) = runTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = Order(
            id = OrderId("12345"),
            secCode = "xyz",
            agreementNumber = "xyz",
            quantity = BigDecimal.valueOf(1500),
            price = BigDecimal.valueOf(100),
            operationType = OrderSide.BUY,
            lock = OrderLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(OrderState.FAILING, ctx.state)
    assertEquals(Order(), ctx.orderResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
