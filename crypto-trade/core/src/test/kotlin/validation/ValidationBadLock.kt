package ru.otus.otuskotlin.crypto.trade.core.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import java.math.BigDecimal
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationLockCorrect(command: OrderCommand, processor: OrderProcessor) = runTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = Order(
            id = OrderId("123-234-abc-ABC"),
            secCode = "abc",
            agreementNumber = "abc",
            operationType = OrderSide.BUY,
            lock = OrderLock("123-234-abc-ABC"),
            quantity = BigDecimal.valueOf(5),
            price = BigDecimal.valueOf(45000)
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(OrderState.FAILING, ctx.state)
}

fun validationLockTrim(command: OrderCommand, processor: OrderProcessor) = runTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = Order(
            id = OrderId("123-234-abc-ABC"),
            secCode = "abc",
            agreementNumber = "abc",
            operationType = OrderSide.BUY,
            lock = OrderLock(" \n\t 123-234-abc-ABC \n\t "),
            quantity = BigDecimal.valueOf(5),
            price = BigDecimal.valueOf(45000)
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(OrderState.FAILING, ctx.state)
}

fun validationLockEmpty(command: OrderCommand, processor: OrderProcessor) = runTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = Order(
            id = OrderId("123-234-abc-ABC"),
            secCode = "abc",
            agreementNumber = "abc",
            operationType = OrderSide.BUY,
            lock = OrderLock(""),
            quantity = BigDecimal.valueOf(5),
            price = BigDecimal.valueOf(45000)
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(OrderState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationLockFormat(command: OrderCommand, processor: OrderProcessor) = runTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = Order(
            id = OrderId("123-234-abc-ABC"),
            secCode = "abc",
            agreementNumber = "abc",
            operationType = OrderSide.BUY,
            lock = OrderLock("!@#\$%^&*(),.{}"),
            quantity = BigDecimal.valueOf(5),
            price = BigDecimal.valueOf(45000)
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(OrderState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
