package ru.otus.otuskotlin.crypto.trade.core.validation

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderFilter
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.cor.rootChain
import validation.runBizTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {

    @Test
    fun emptyString() = runBizTest {
        val ctx = OrderContext(state = OrderState.RUNNING, orderFilterValidating = OrderFilter(searchString = ""))
        chain.exec(ctx)
        assertEquals(OrderState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runBizTest {
        val ctx = OrderContext(state = OrderState.RUNNING, orderFilterValidating = OrderFilter(searchString = "  "))
        chain.exec(ctx)
        assertEquals(OrderState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runBizTest {
        val ctx = OrderContext(state = OrderState.RUNNING, orderFilterValidating = OrderFilter(searchString = "12"))
        chain.exec(ctx)
        assertEquals(OrderState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runBizTest {
        val ctx = OrderContext(state = OrderState.RUNNING, orderFilterValidating = OrderFilter(searchString = "123"))
        chain.exec(ctx)
        assertEquals(OrderState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runBizTest {
        val ctx = OrderContext(
            state = OrderState.RUNNING,
            orderFilterValidating = OrderFilter(searchString = "12".repeat(51))
        )
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
