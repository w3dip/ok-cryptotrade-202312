package rest.order.v1

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
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
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class OrderApiV1Test {

    @Test
    fun create() = orderApiV1TestApplication(
        func = "create",
        request = OrderCreateRequest(
            order = OrderCreateObject(
                secCode = "BTC",
                agreementNumber = "A001",
                quantity = BigDecimal.valueOf(5),
                price = BigDecimal.valueOf(65000),
                operationType = OrderSide.BUY
            ),
            debug = OrderDebug(
                mode = OrderRequestDebugMode.STUB,
                stub = OrderRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<OrderCreateResponse>()
        assertEquals(200, response.status.value)
        assertNotNull(responseObj.order?.id)
    }

    @Test
    fun read() = orderApiV1TestApplication(
        func = "read",
        request = OrderReadRequest(
            order = OrderReadObject("102"),
            debug = OrderDebug(
                mode = OrderRequestDebugMode.STUB,
                stub = OrderRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<OrderReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("102", responseObj.order?.id)
    }

    @Test
    fun update() = orderApiV1TestApplication(
        func = "update",
        request = OrderUpdateRequest(
            order = OrderUpdateObject(
                id = "102",
                secCode = "BTC",
                agreementNumber = "A001",
                quantity = BigDecimal.valueOf(5),
                price = BigDecimal.valueOf(65000),
                operationType = OrderSide.BUY
            ),
            debug = OrderDebug(
                mode = OrderRequestDebugMode.STUB,
                stub = OrderRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<OrderUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("102", responseObj.order?.id)
    }

    @Test
    fun delete() = orderApiV1TestApplication(
        func = "delete",
        request = OrderDeleteRequest(
            order = OrderDeleteObject(
                id = "102",
            ),
            debug = OrderDebug(
                mode = OrderRequestDebugMode.STUB,
                stub = OrderRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<OrderDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("102", responseObj.order?.id)
    }

    @Test
    fun search() = orderApiV1TestApplication(
        func = "search",
        request = OrderSearchRequest(
            orderFilter = OrderSearchFilter(),
            debug = OrderDebug(
                mode = OrderRequestDebugMode.STUB,
                stub = OrderRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<OrderSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotNull(responseObj.orders?.first()?.id)
    }

    private fun orderApiV1TestApplication(
        func: String,
        request: IRequest,
        function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(AppSettings()) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                    enable(SerializationFeature.INDENT_OUTPUT)
                    writerWithDefaultPrettyPrinter()
                }
            }
        }
        val response = client.post("/v1/order/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}
