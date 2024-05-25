package ru.otus.otuskotlin.crypto.trade.repo.tests

import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderRequest
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseErr
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseErrWithData
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.crypto.trade.repo.common.OrderRepoInitialized
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoOrderUpdateTest {
    abstract val repo: OrderRepoInitialized
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = OrderId("ad-repo-update-not-found")
    protected val lockBad = OrderLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = OrderLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        Order(
            id = updateSucc.id,
            secCode = "update sec code",
            agreementNumber = "update agreement number",
            quantity = BigDecimal.valueOf(1000),
            price = BigDecimal.valueOf(12000),
            userId = OrderUserId("user-123"),
            operationType = OrderSide.BUY,
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = Order(
        id = updateIdNotFound,
        secCode = "update object not found",
        agreementNumber = "update object not found description",
        quantity = BigDecimal.valueOf(1000),
        price = BigDecimal.valueOf(12000),
        userId = OrderUserId("user-123"),
        operationType = OrderSide.BUY,
        lock = initObjects.first().lock,
    )
    private val reqUpdateConc by lazy {
        Order(
            id = updateConc.id,
            secCode = "update object not found",
            agreementNumber = "update object not found description",
            quantity = BigDecimal.valueOf(1000),
            price = BigDecimal.valueOf(12000),
            userId = OrderUserId("user-123"),
            operationType = OrderSide.BUY,
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateOrder(DbOrderRequest(reqUpdateSucc))
        println("ERRORS: ${(result as? DbOrderResponseErr)?.errors}")
        println("ERRORSWD: ${(result as? DbOrderResponseErrWithData)?.errors}")
        assertIs<DbOrderResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.secCode, result.data.secCode)
        assertEquals(reqUpdateSucc.agreementNumber, result.data.agreementNumber)
        assertEquals(reqUpdateSucc.operationType, result.data.operationType)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateOrder(DbOrderRequest(reqUpdateNotFound))
        assertIs<DbOrderResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateOrder(DbOrderRequest(reqUpdateConc))
        assertIs<DbOrderResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitOrders("update") {
        override val initObjects: List<Order> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
