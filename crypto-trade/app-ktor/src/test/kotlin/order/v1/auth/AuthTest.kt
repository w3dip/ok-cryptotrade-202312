package order.v1.auth

import CorSettings
import apiV1Mapper
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.crypto.trade.api.v1.models.*
import ru.otus.otuskotlin.crypto.trade.app.AppSettings
import ru.otus.otuskotlin.crypto.trade.app.module
import ru.otus.otuskotlin.crypto.trade.repo.inmemory.OrderRepoInMemory
import ru.otus.otuskotlin.marketplace.app.ktor.auth.addAuth
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun invalidAudience() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }
        }
        application { module(AppSettings(corSettings = CorSettings(repoTest = OrderRepoInMemory()))) }
        val response = client.post("/v1/order/create") {
            addAuth(groups = emptyList())
            contentType(ContentType.Application.Json)
            setBody(
                OrderCreateRequest(
                    order = OrderCreateObject(
                        secCode = "BTC",
                        agreementNumber = "A001",
                        quantity = BigDecimal.valueOf(5),
                        price = BigDecimal.valueOf(65000),
                        operationType = OrderSide.BUY
                    ),
                    debug = OrderDebug(mode = OrderRequestDebugMode.TEST)
                )
            )
        }
        val orderObj = response.body<OrderCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(ResponseResult.ERROR, orderObj.result)
        assertEquals("access-create", orderObj.errors?.first()?.code)
    }
}
