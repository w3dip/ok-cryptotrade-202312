package ru.otus.otuskotlin.crypto.trade.repo.tests

import ru.otus.otuskotlin.crypto.trade.common.models.Order
import ru.otus.otuskotlin.crypto.trade.common.models.OrderError
import ru.otus.otuskotlin.crypto.trade.common.models.OrderId
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderIdRequest
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseErr
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.crypto.trade.repo.common.OrderRepoInitialized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class OrderRepoReadTest {
    abstract val repo: OrderRepoInitialized
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readOrder(DbOrderIdRequest(readSucc.id))

        assertIs<DbOrderResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        println("REQUESTING")
        val result = repo.readOrder(DbOrderIdRequest(notFoundId))
        println("RESULT: $result")

        assertIs<DbOrderResponseErr>(result)
        println("ERRORS: ${result.errors}")
        val error: OrderError? = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitOrders("read") {
        override val initObjects: List<Order> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = OrderId("order-repo-read-notFound")

    }
}
