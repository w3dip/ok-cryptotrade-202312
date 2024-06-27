package ru.otus.otuskotlin.crypto.trade.core.validation

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.common.models.OrderWorkMode
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub
import ru.otus.otuskotlin.marketplace.biz.addTestPrincipal
import validation.runValidationTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = OrderStub.get()

fun validationAgreementNumberCorrect(command: OrderCommand, processor: OrderProcessor) = runValidationTest {
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
    assertContains(ctx.orderValidated.agreementNumber, "A001")
}

fun validationAgreementNumberTrim(command: OrderCommand, processor: OrderProcessor) = runValidationTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = OrderStub.prepareResult {
            agreementNumber = " \n\tabc \n\t"
        },
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(OrderState.FAILING, ctx.state)
    assertEquals("abc", ctx.orderValidated.agreementNumber)
}

fun validationAgreementNumberEmpty(command: OrderCommand, processor: OrderProcessor) = runValidationTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = OrderStub.prepareResult {
            agreementNumber = ""
        },
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(OrderState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("agreementNumber", error?.field)
    assertContains(error?.message ?: "", "agreementNumber")
}

fun validationAgreementNumberSymbols(command: OrderCommand, processor: OrderProcessor) = runValidationTest {
    val ctx = OrderContext(
        command = command,
        state = OrderState.NONE,
        workMode = OrderWorkMode.TEST,
        orderRequest = OrderStub.prepareResult {
            agreementNumber = "!@#$%^&*(),.{}"
        },
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(OrderState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("agreementNumber", error?.field)
    assertContains(error?.message ?: "", "agreementNumber")
}
