package ru.otus.otuskotlin.crypto.trade.api.v1

import apiV1Mapper
import ru.otus.otuskotlin.crypto.trade.api.v1.models.IResponse
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderCreateResponse
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderResponseObject
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderSide
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = OrderCreateResponse(
        order = OrderResponseObject(
            operationType = OrderSide.BUY,
            secCode = "BTC",
            agreementNumber = "A001",
            quantity = BigDecimal.valueOf(5),
            price = BigDecimal.valueOf(65000)
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"secCode\":\\s*\"BTC\""))
        assertContains(json, Regex("\"agreementNumber\":\\s*\"A001\""))
        assertContains(json, Regex("\"quantity\":\\s*5"))
        assertContains(json, Regex("\"price\":\\s*65000"))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as OrderCreateResponse
        assertEquals(response, obj)
    }
}
