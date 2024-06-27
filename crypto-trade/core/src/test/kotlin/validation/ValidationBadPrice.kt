package ru.otus.otuskotlin.crypto.trade.core.validation

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.common.models.OrderWorkMode
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub
import ru.otus.otuskotlin.marketplace.biz.addTestPrincipal
import validation.runValidationTest
import java.math.BigDecimal
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = OrderStub.get()

fun validationPriceCorrect(command: OrderCommand, processor: OrderProcessor) = runValidationTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = OrderStub.get(),
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(OrderState.FAILING, ctx.state)
    assertEquals(OrderStub.get().quantity, ctx.orderValidated.quantity)
}

fun validationPriceNegative(command: OrderCommand, processor: OrderProcessor) = runValidationTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = OrderStub.prepareResult {
            price = BigDecimal.valueOf(-25000)
        },
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(OrderState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("price", error?.field)
    assertContains(error?.message ?: "", "price")
}

fun validationPriceZero(command: OrderCommand, processor: OrderProcessor) = runValidationTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = OrderStub.prepareResult {
            price = BigDecimal.ZERO
        },
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(OrderState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("price", error?.field)
    assertContains(error?.message ?: "", "price")
}
