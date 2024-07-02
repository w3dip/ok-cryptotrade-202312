package ru.otus.otuskotlin.crypto.trade.core.validation

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderFilter
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.cor.rootChain
import ru.otus.otuskotlin.marketplace.biz.addTestPrincipal
import validation.runValidationTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {

    @Test
    fun emptyString() = runValidationTest {
        val ctx = OrderContext(state = OrderState.RUNNING, orderFilterValidating = OrderFilter(searchString = ""))
        chain.exec(ctx)
        assertEquals(OrderState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runValidationTest {
        val ctx = OrderContext(state = OrderState.RUNNING, orderFilterValidating = OrderFilter(searchString = "  "))
        ctx.addTestPrincipal()
        chain.exec(ctx)
        assertEquals(OrderState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runValidationTest {
        val ctx = OrderContext(state = OrderState.RUNNING, orderFilterValidating = OrderFilter(searchString = "12"))
        ctx.addTestPrincipal()
        chain.exec(ctx)
        assertEquals(OrderState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runValidationTest {
        val ctx = OrderContext(state = OrderState.RUNNING, orderFilterValidating = OrderFilter(searchString = "123"))
        ctx.addTestPrincipal()
        chain.exec(ctx)
        assertEquals(OrderState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runValidationTest {
        val ctx = OrderContext(
            state = OrderState.RUNNING,
            orderFilterValidating = OrderFilter(searchString = "12".repeat(51))
        )
        ctx.addTestPrincipal()
        chain.exec(ctx)
        assertEquals(OrderState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}
