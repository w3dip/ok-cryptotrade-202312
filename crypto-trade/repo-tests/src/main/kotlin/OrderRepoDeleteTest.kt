package ru.otus.otuskotlin.crypto.trade.repo.tests

import ru.otus.otuskotlin.crypto.trade.common.models.Order
import ru.otus.otuskotlin.crypto.trade.common.models.OrderId
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderIdRequest
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseErr
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseErrWithData
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.crypto.trade.repo.common.OrderRepoInitialized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class OrderRepoDeleteTest {
    abstract val repo: OrderRepoInitialized
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = OrderId("order-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteOrder(DbOrderIdRequest(deleteSucc.id, lock = lockOld))
        assertIs<DbOrderResponseOk>(result)
        assertEquals(deleteSucc.secCode, result.data.secCode)
        assertEquals(deleteSucc.agreementNumber, result.data.agreementNumber)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readOrder(DbOrderIdRequest(notFoundId, lock = lockOld))

        assertIs<DbOrderResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deleteOrder(DbOrderIdRequest(deleteConc.id, lock = lockBad))

        assertIs<DbOrderResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitOrders("delete") {
        override val initObjects: List<Order> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
