package ru.otus.otuskotlin.crypto.trade.core.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.Order
import ru.otus.otuskotlin.crypto.trade.common.models.OrderFilter
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSecCodeHasContentTest {
    @Test
    fun emptyString() = runTest {
        val ctx = OrderContext(state = OrderState.RUNNING, orderValidating = Order(secCode = ""))
        chain.exec(ctx)
        assertEquals(OrderState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = OrderContext(state = OrderState.RUNNING, orderValidating = Order(secCode = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(OrderState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-secCode-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = OrderContext(state = OrderState.RUNNING, orderFilterValidating = OrderFilter(searchString = "Ж"))
        chain.exec(ctx)
        assertEquals(OrderState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateSecCodeHasContent("")
        }.build()
    }
}
