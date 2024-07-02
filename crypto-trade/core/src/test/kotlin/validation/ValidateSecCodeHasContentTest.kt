package ru.otus.otuskotlin.crypto.trade.core.validation

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.Order
import ru.otus.otuskotlin.crypto.trade.common.models.OrderFilter
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.cor.rootChain
import ru.otus.otuskotlin.marketplace.biz.addTestPrincipal
import validation.runValidationTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSecCodeHasContentTest {
    @Test
    fun emptyString() = runValidationTest {
        val ctx = OrderContext(state = OrderState.RUNNING, orderValidating = Order(secCode = ""))
        ctx.addTestPrincipal()
        chain.exec(ctx)
        assertEquals(OrderState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runValidationTest {
        val ctx = OrderContext(state = OrderState.RUNNING, orderValidating = Order(secCode = "12!@#$%^&*()_+-="))
        ctx.addTestPrincipal()
        chain.exec(ctx)
        assertEquals(OrderState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-secCode-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runValidationTest {
        val ctx = OrderContext(state = OrderState.RUNNING, orderFilterValidating = OrderFilter(searchString = "Ж"))
        ctx.addTestPrincipal()
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
