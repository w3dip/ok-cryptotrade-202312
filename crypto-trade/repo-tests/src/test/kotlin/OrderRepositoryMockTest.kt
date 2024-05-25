package ru.otus.otuskotlin.crypto.trade.repo.tests

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.crypto.trade.common.models.Order
import ru.otus.otuskotlin.crypto.trade.common.repo.*
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class OrderRepositoryMockTest {
    private val repo = OrderRepositoryMock(
        invokeCreateOrder = { DbOrderResponseOk(OrderStub.prepareResult { secCode = "create" }) },
        invokeReadOrder = { DbOrderResponseOk(OrderStub.prepareResult { secCode = "read" }) },
        invokeUpdateOrder = { DbOrderResponseOk(OrderStub.prepareResult { secCode = "update" }) },
        invokeDeleteOrder = { DbOrderResponseOk(OrderStub.prepareResult { secCode = "delete" }) },
        invokeSearchOrder = { DbOrdersResponseOk(listOf(OrderStub.prepareResult { secCode = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createOrder(DbOrderRequest(Order()))
        assertIs<DbOrderResponseOk>(result)
        assertEquals("create", result.data.secCode)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readOrder(DbOrderIdRequest(Order()))
        assertIs<DbOrderResponseOk>(result)
        assertEquals("read", result.data.secCode)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateOrder(DbOrderRequest(Order()))
        assertIs<DbOrderResponseOk>(result)
        assertEquals("update", result.data.secCode)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteOrder(DbOrderIdRequest(Order()))
        assertIs<DbOrderResponseOk>(result)
        assertEquals("delete", result.data.secCode)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchOrder(DbOrderFilterRequest())
        assertIs<DbOrdersResponseOk>(result)
        assertEquals("search", result.data.first().secCode)
    }

}
