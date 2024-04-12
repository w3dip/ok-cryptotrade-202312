package ru.otus.otuskotlin.crypto.trade.mappers

import ru.otus.otuskotlin.crypto.trade.api.v1.models.*
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderSide.BUY
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderSide.SELL
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.exceptions.UnknownOrderCommand
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.models.OrderSide
import java.math.BigDecimal.ZERO

fun OrderContext.toTransportOrder(): IResponse = when (val cmd = command) {
    OrderCommand.CREATE -> toTransportCreate()
    OrderCommand.READ -> toTransportRead()
    OrderCommand.UPDATE -> toTransportUpdate()
    OrderCommand.DELETE -> toTransportDelete()
    OrderCommand.SEARCH -> toTransportSearch()
    OrderCommand.NONE -> throw UnknownOrderCommand(cmd)
}

fun OrderContext.toTransportCreate() = OrderCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    order = orderResponse.toTransportOrder()
)

fun OrderContext.toTransportRead() = OrderReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    order = orderResponse.toTransportOrder()
)

fun OrderContext.toTransportUpdate() = OrderUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    order = orderResponse.toTransportOrder()
)

fun OrderContext.toTransportDelete() = OrderDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    order = orderResponse.toTransportOrder()
)

fun OrderContext.toTransportSearch() = OrderSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    orders = ordersResponse.toTransportOrder()
)

fun List<Order>.toTransportOrder(): List<OrderResponseObject>? = this
    .map { it.toTransportOrder() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun Order.toTransportOrder(): OrderResponseObject = OrderResponseObject(
    id = id.takeIf { it != OrderId.NONE }?.asString(),
    secCode = secCode.takeIf { it.isNotBlank() },
    agreementNumber = agreementNumber.takeIf { it.isNotBlank() },
    quantity = quantity.takeIf { it != ZERO },
    price = price.takeIf { it != ZERO },
    userId = userId.takeIf { it != OrderUserId.NONE }?.asString(),
    operationType = operationType.toTransportOrder(),
    permissions = permissionsClient.toTransportOrder(),
)

private fun Set<OrderPermissionClient>.toTransportOrder(): Set<OrderPermissions>? = this
    .map { it.toTransportOrder() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun OrderPermissionClient.toTransportOrder() = when (this) {
    OrderPermissionClient.READ -> OrderPermissions.READ
    OrderPermissionClient.UPDATE -> OrderPermissions.UPDATE
    OrderPermissionClient.DELETE -> OrderPermissions.DELETE
}

private fun OrderSide.toTransportOrder(): ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderSide? = when (this) {
    OrderSide.BUY -> BUY
    OrderSide.SELL -> SELL
    OrderSide.NONE -> null
}

private fun List<OrderError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportOrder() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun OrderError.toTransportOrder() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun OrderState.toResult(): ResponseResult? = when (this) {
    OrderState.RUNNING -> ResponseResult.SUCCESS
    OrderState.FAILING -> ResponseResult.ERROR
    OrderState.FINISHING -> ResponseResult.SUCCESS
    OrderState.NONE -> null
}
