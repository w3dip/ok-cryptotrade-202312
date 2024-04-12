package ru.otus.otuskotlin.crypto.trade.e2e.test.action.v1

import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderCreateObject
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderDebug
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderRequestDebugMode
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderRequestDebugStubs
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderSide.BUY
import java.math.BigDecimal

val debug = OrderDebug(mode = OrderRequestDebugMode.STUB, stub = OrderRequestDebugStubs.SUCCESS)

val someCreateOrder = OrderCreateObject(
    secCode = "BTC",
    agreementNumber = "A001",
    quantity = BigDecimal.valueOf(5),
    price = BigDecimal.valueOf(65000),
    operationType = BUY
)
