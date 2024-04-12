package ru.otus.otuskotlin.crypto.trade.mappers

import ru.otus.otuskotlin.crypto.trade.api.v1.models.*
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderSide.BUY
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderSide.SELL
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.models.OrderSide
import ru.otus.otuskotlin.crypto.trade.common.stubs.OrderStubs
import ru.otus.otuskotlin.crypto.trade.mappers.exceptions.UnknownRequestClass
import java.math.BigDecimal.ZERO

fun OrderContext.fromTransport(request: IRequest) = when (request) {
    is OrderCreateRequest -> fromTransport(request)
    is OrderReadRequest -> fromTransport(request)
    is OrderUpdateRequest -> fromTransport(request)
    is OrderDeleteRequest -> fromTransport(request)
    is OrderSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toOrderId() = this?.let { OrderId(it) } ?: OrderId.NONE
private fun String?.toOrderWithId() = Order(id = this.toOrderId())
private fun String?.toOrderLock() = this?.let { OrderLock(it) } ?: OrderLock.NONE

private fun OrderDebug?.transportToWorkMode(): OrderWorkMode = when (this?.mode) {
    OrderRequestDebugMode.PROD -> OrderWorkMode.PROD
    OrderRequestDebugMode.TEST -> OrderWorkMode.TEST
    OrderRequestDebugMode.STUB -> OrderWorkMode.STUB
    null -> OrderWorkMode.PROD
}

private fun OrderDebug?.transportToStubCase(): OrderStubs = when (this?.stub) {
    OrderRequestDebugStubs.SUCCESS -> OrderStubs.SUCCESS
    OrderRequestDebugStubs.NOT_FOUND -> OrderStubs.NOT_FOUND
    OrderRequestDebugStubs.BAD_ORDER_ID -> OrderStubs.BAD_ORDER_ID
    OrderRequestDebugStubs.BAD_SEC_CODE -> OrderStubs.BAD_SEC_CODE
    OrderRequestDebugStubs.BAD_AGREEMENT_NUMBER -> OrderStubs.BAD_AGREEMENT_NUMBER
    OrderRequestDebugStubs.BAD_QUANTITY -> OrderStubs.BAD_QUANTITY
    OrderRequestDebugStubs.BAD_PRICE -> OrderStubs.BAD_PRICE
    OrderRequestDebugStubs.CANNOT_DELETE -> OrderStubs.CANNOT_DELETE
    OrderRequestDebugStubs.BAD_SEARCH_STRING -> OrderStubs.BAD_SEARCH_STRING
    null -> OrderStubs.NONE
}

fun OrderContext.fromTransport(request: OrderCreateRequest) {
    command = OrderCommand.CREATE
    orderRequest = request.order?.toInternal() ?: Order()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun OrderContext.fromTransport(request: OrderReadRequest) {
    command = OrderCommand.READ
    orderRequest = request.order.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun OrderReadObject?.toInternal(): Order = if (this != null) {
    Order(id = id.toOrderId())
} else {
    Order()
}

fun OrderContext.fromTransport(request: OrderUpdateRequest) {
    command = OrderCommand.UPDATE
    orderRequest = request.order?.toInternal() ?: Order()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun OrderContext.fromTransport(request: OrderDeleteRequest) {
    command = OrderCommand.DELETE
    orderRequest = request.order.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun OrderDeleteObject?.toInternal(): Order = if (this != null) {
    Order(
        id = id.toOrderId(),
        lock = lock.toOrderLock(),
    )
} else {
    Order()
}

fun OrderContext.fromTransport(request: OrderSearchRequest) {
    command = OrderCommand.SEARCH
    orderFilterRequest = request.orderFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun OrderSearchFilter?.toInternal(): OrderFilter = OrderFilter(
    searchString = this?.searchString ?: ""
)

private fun OrderCreateObject.toInternal(): Order = Order(
    secCode = this.secCode ?: "",
    agreementNumber = this.agreementNumber ?: "",
    quantity = this.quantity ?: ZERO,
    price = this.price ?: ZERO,
    operationType = this.operationType.fromTransport(),
)

private fun OrderUpdateObject.toInternal(): Order = Order(
    id = this.id.toOrderId(),
    secCode = this.secCode ?: "",
    agreementNumber = this.agreementNumber ?: "",
    quantity = this.quantity ?: ZERO,
    price = this.price ?: ZERO,
    operationType = this.operationType.fromTransport(),
    lock = lock.toOrderLock(),
)

private fun ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderSide?.fromTransport(): OrderSide = when (this) {
    BUY -> OrderSide.BUY
    SELL -> OrderSide.SELL
    null -> OrderSide.NONE
}

