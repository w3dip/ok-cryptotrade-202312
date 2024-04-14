package rest.order.v1

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.crypto.trade.api.v1.models.*
import ru.otus.otuskotlin.crypto.trade.app.AppSettings
import ru.otus.otuskotlin.crypto.trade.app.rest.order.v1.controllerHelper
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.mappers.fromTransport
import ru.otus.otuskotlin.crypto.trade.mappers.toTransportOrder
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderControllerExtTest {

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

    private val appSettings = AppSettings(emptyList(), OrderProcessor())

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createOrder(appSettings: AppSettings) {
        respond(
            appSettings.controllerHelper(
                { fromTransport(receive<OrderCreateRequest>()) },
                { toTransportOrder() },
                OrderControllerExtTest::class,
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
