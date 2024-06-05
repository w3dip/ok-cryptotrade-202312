package ru.otus.otuskotlin.crypto.trade.core.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand
import ru.otus.otuskotlin.crypto.trade.common.models.OrderFilter
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.common.models.OrderWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ValidationSearchTest : BaseValidationTest() {
    override val command = OrderCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = OrderContext(
            command = command,
            state = OrderState.NONE,
            workMode = OrderWorkMode.TEST,
            orderFilterRequest = OrderFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(OrderState.FAILING, ctx.state)
    }
}
