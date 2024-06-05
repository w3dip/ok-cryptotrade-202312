package ru.otus.otuskotlin.crypto.trade.repo.tests

import ru.otus.otuskotlin.crypto.trade.common.models.Order
import ru.otus.otuskotlin.crypto.trade.common.models.OrderSide
import ru.otus.otuskotlin.crypto.trade.common.models.OrderUserId
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderFilterRequest
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrdersResponseOk
import ru.otus.otuskotlin.crypto.trade.repo.common.OrderRepoInitialized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class OrderRepoSearchTest {
    abstract val repo: OrderRepoInitialized

    protected open val initializedObjects: List<Order> = initObjects

    @Test
    fun searchUser() = runRepoTest {
        val result = repo.searchOrder(DbOrderFilterRequest(userId = searchUserId))
        assertIs<DbOrdersResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    @Test
    fun searchOperationType() = runRepoTest {
        val result = repo.searchOrder(DbOrderFilterRequest(operationType = OrderSide.SELL))
        assertIs<DbOrdersResponseOk>(result)
        val expected = listOf(initializedObjects[4]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object : BaseInitOrders("search") {

        val searchUserId = OrderUserId("user-124")
        override val initObjects: List<Order> = listOf(
            createInitTestModel("order1"),
            createInitTestModel("order2", userId = searchUserId),
            createInitTestModel("order3", operationType = OrderSide.BUY),
            createInitTestModel("order4", userId = searchUserId),
            createInitTestModel("order5", operationType = OrderSide.SELL),
        )
    }
}
