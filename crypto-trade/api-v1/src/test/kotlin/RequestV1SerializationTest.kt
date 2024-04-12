package ru.otus.otuskotlin.crypto.trade.api.v1

import apiV1Mapper
import ru.otus.otuskotlin.crypto.trade.api.v1.models.*
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = OrderCreateRequest(
        debug = OrderDebug(
            mode = OrderRequestDebugMode.STUB,
            stub = OrderRequestDebugStubs.BAD_SEC_CODE
        ),
        order = OrderCreateObject(
            operationType = OrderSide.BUY,
            secCode = "BTC",
            agreementNumber = "A001",
            quantity = BigDecimal.valueOf(5),
            price = BigDecimal.valueOf(65000)
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"secCode\":\\s*\"BTC\""))
        assertContains(json, Regex("\"agreementNumber\":\\s*\"A001\""))
        assertContains(json, Regex("\"quantity\":\\s*5"))
        assertContains(json, Regex("\"price\":\\s*65000"))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badSecCode\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as OrderCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"order": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, OrderCreateRequest::class.java)

        assertEquals(null, obj.order)
    }
}
