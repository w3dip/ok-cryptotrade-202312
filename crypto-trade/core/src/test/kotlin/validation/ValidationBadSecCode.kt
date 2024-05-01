package ru.otus.otuskotlin.crypto.trade.core.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub
import java.math.BigDecimal
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = OrderStub.get()

fun validationSecCodeCorrect(command: OrderCommand, processor: OrderProcessor) = runTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = Order(
            id = stub.id,
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
    assertEquals("abc", ctx.orderValidated.secCode)
}

fun validationSecCodeTrim(command: OrderCommand, processor: OrderProcessor) = runTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = Order(
            id = stub.id,
            secCode = " \n\t abc \t\n ",
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
    assertEquals("abc", ctx.orderValidated.secCode)
}

fun validationSecCodeEmpty(command: OrderCommand, processor: OrderProcessor) = runTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = Order(
            id = stub.id,
            secCode = "",
            agreementNumber = "abc",
            operationType = OrderSide.BUY,
            lock = OrderLock("123-234-abc-ABC"),
            quantity = BigDecimal.valueOf(5),
            price = BigDecimal.valueOf(45000)
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(OrderState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("secCode", error?.field)
    assertContains(error?.message ?: "", "secCode")
}

fun validationSecCodeSymbols(command: OrderCommand, processor: OrderProcessor) = runTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = Order(
            id = OrderId("123"),
            secCode = "!@#$%^&*(),.{}",
            agreementNumber = "abc",
            operationType = OrderSide.BUY,
            lock = OrderLock("123-234-abc-ABC"),
            quantity = BigDecimal.valueOf(5),
            price = BigDecimal.valueOf(45000)
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(OrderState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("secCode", error?.field)
    assertContains(error?.message ?: "", "secCode")
}
