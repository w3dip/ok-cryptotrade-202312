package ru.otus.otuskotlin.crypto.trade.core.validation

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand
import ru.otus.otuskotlin.crypto.trade.common.models.OrderId
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.common.models.OrderWorkMode
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub
import validation.runValidationTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: OrderCommand, processor: OrderProcessor) = runValidationTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = OrderStub.get(),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(OrderState.FAILING, ctx.state)
}

fun validationIdTrim(command: OrderCommand, processor: OrderProcessor) = runValidationTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = OrderStub.prepareResult {
            id = OrderId(" \n\t ${id.asString()} \n\t ")
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(OrderState.FAILING, ctx.state)
}

fun validationIdEmpty(command: OrderCommand, processor: OrderProcessor) = runValidationTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = OrderStub.prepareResult {
            id = OrderId("")
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(OrderState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: OrderCommand, processor: OrderProcessor) = runValidationTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = OrderStub.prepareResult {
            id = OrderId("!@#\$%^&*(),.{}")
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(OrderState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
