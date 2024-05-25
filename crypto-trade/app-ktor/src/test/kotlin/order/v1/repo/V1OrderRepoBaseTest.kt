package ru.otus.otuskotlin.marketplace.app.ktor.repo

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.crypto.trade.api.v1.models.*
import ru.otus.otuskotlin.crypto.trade.app.AppSettings
import ru.otus.otuskotlin.crypto.trade.app.module
import ru.otus.otuskotlin.crypto.trade.common.models.OrderId
import ru.otus.otuskotlin.crypto.trade.common.models.OrderLock
import ru.otus.otuskotlin.crypto.trade.common.models.OrderSide
import ru.otus.otuskotlin.crypto.trade.mappers.toTransportCreate
import ru.otus.otuskotlin.crypto.trade.mappers.toTransportDelete
import ru.otus.otuskotlin.crypto.trade.mappers.toTransportRead
import ru.otus.otuskotlin.crypto.trade.mappers.toTransportUpdate
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class V1OrderRepoBaseTest {
    abstract val workMode: OrderRequestDebugMode
    abstract val appSettingsCreate: AppSettings
    abstract val appSettingsRead: AppSettings
    abstract val appSettingsUpdate: AppSettings
    abstract val appSettingsDelete: AppSettings
    abstract val appSettingsSearch: AppSettings

    protected val uuidOld = "10000000-0000-0000-0000-000000000001"
    protected val uuidNew = "10000000-0000-0000-0000-000000000002"
    protected val uuidSup = "10000000-0000-0000-0000-000000000003"
    protected val initOrder = OrderStub.prepareResult {
        id = OrderId(uuidOld)
        operationType = OrderSide.BUY
        lock = OrderLock(uuidOld)
    }
    protected val initOrderSell = OrderStub.prepareResult {
        id = OrderId(uuidSup)
        operationType = OrderSide.SELL
    }


    @Test
    fun create() {
        val order = initOrder.toTransportCreate()
        v1TestApplication(
            conf = appSettingsCreate,
            func = "create",
            request = OrderCreateRequest(
                order = order,
                debug = OrderDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<IResponse>() as OrderCreateResponse
            assertEquals(200, response.status.value)
            assertEquals(uuidNew, responseObj.order?.id)
            assertEquals(order.secCode, responseObj.order?.secCode)
            assertEquals(order.agreementNumber, responseObj.order?.agreementNumber)
            assertEquals(order.quantity, responseObj.order?.quantity)
            assertEquals(order.price, responseObj.order?.price)
            assertEquals(order.operationType, responseObj.order?.operationType)
        }
    }

    @Test
    fun read() {
        val order = initOrder.toTransportRead()
        v1TestApplication(
            conf = appSettingsRead,
            func = "read",
            request = OrderReadRequest(
                order = order,
                debug = OrderDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<IResponse>() as OrderReadResponse
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.order?.id)
        }
    }

    @Test
    fun update() {
        val order = initOrder.toTransportUpdate()
        v1TestApplication(
            conf = appSettingsUpdate,
            func = "update",
            request = OrderUpdateRequest(
                order = order,
                debug = OrderDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<IResponse>() as OrderUpdateResponse
            assertEquals(200, response.status.value)
            assertEquals(order.id, responseObj.order?.id)
            assertEquals(order.secCode, responseObj.order?.secCode)
            assertEquals(order.agreementNumber, responseObj.order?.agreementNumber)
            assertEquals(order.quantity, responseObj.order?.quantity)
            assertEquals(order.price, responseObj.order?.price)
            assertEquals(order.operationType, responseObj.order?.operationType)
            assertEquals(uuidNew, responseObj.order?.lock)
        }
    }

    @Test
    fun delete() {
        val order = initOrder.toTransportDelete()
        v1TestApplication(
            conf = appSettingsDelete,
            func = "delete",
            request = OrderDeleteRequest(
                order = order,
                debug = OrderDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<IResponse>() as OrderDeleteResponse
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.order?.id)
        }
    }

    @Test
    fun search() = v1TestApplication(
        conf = appSettingsSearch,
        func = "search",
        request = OrderSearchRequest(
            orderFilter = OrderSearchFilter(),
            debug = OrderDebug(mode = workMode),
        ),
    ) { response ->
        val responseObj = response.body<IResponse>() as OrderSearchResponse
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.orders?.size)
        assertEquals(uuidOld, responseObj.orders?.first()?.id)
    }

    private inline fun <reified T : IRequest> v1TestApplication(
        conf: AppSettings,
        func: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(appSettings = conf) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson()
            }
        }
        val response = client.post("/v1/order/$func") {
            contentType(ContentType.Application.Json)
            header("X-Trace-Id", "12345")
            setBody(request)
        }
        function(response)
    }
}
