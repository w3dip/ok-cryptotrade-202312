package ru.otus.otuskotlin.crypto.trade.e2e.test.action.v1

import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderCreateObject
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderDebug
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderRequestDebugMode
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderRequestDebugStubs
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderSide.BUY
import ru.otus.otuskotlin.crypto.trade.e2e.test.TestDebug
import java.math.BigDecimal

val debugStubV1 = OrderDebug(mode = OrderRequestDebugMode.STUB, stub = OrderRequestDebugStubs.SUCCESS)

val someCreateOrder = OrderCreateObject(
    secCode = "BTC",
    agreementNumber = "A001",
    quantity = BigDecimal.valueOf(2000.00),
    price = BigDecimal.valueOf(200000.00),
    operationType = BUY,
)

fun TestDebug.toV1() = when (this) {
    TestDebug.STUB -> debugStubV1
    TestDebug.PROD -> OrderDebug(mode = OrderRequestDebugMode.PROD)
    TestDebug.TEST -> OrderDebug(mode = OrderRequestDebugMode.TEST)
}
