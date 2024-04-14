package ru.otus.otuskotlin.crypto.trade.app.common

import CorSettings
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.crypto.trade.api.v1.models.*
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.mappers.fromTransport
import ru.otus.otuskotlin.crypto.trade.mappers.toTransportOrder
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerHelperTest {

    private val request = OrderCreateRequest(
        order = OrderCreateObject(
            secCode = "BTC",
            agreementNumber = "A001",
            quantity = BigDecimal.valueOf(5),
            price = BigDecimal.valueOf(65000),
            operationType = OrderSide.BUY
        ),
        debug = OrderDebug(mode = OrderRequestDebugMode.STUB, stub = OrderRequestDebugStubs.SUCCESS)
    )


    private val appSettings: CommonAppSettings = object : CommonAppSettings {
        override val corSettings: CorSettings = CorSettings()
        override val processor: OrderProcessor = OrderProcessor(corSettings)
    }

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createOrder(appSettings: CommonAppSettings) {
        respond(
            appSettings.controllerHelper(
                { fromTransport(receive<OrderCreateRequest>()) },
                { toTransportOrder() },
                ControllerHelperTest::class,
                "controller-v1-test"
            )
        )
    }

    @Test
    fun controllerHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createOrder(appSettings) }
        val res = testApp.res as OrderCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}
