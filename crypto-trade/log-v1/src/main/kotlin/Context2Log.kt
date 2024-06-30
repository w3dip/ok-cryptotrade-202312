package ru.otus.otuskotlin.crypto.trade.log.api.v1

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.log.v1.models.*
import java.math.BigDecimal

fun OrderContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-cryptotrade",
    order = toOrderLog(),
    errors = errors.map { it.toLog() },
)

private fun OrderContext.toOrderLog(): OrderLogModel? {
    val orderNone = Order()
    return OrderLogModel(
        requestId = requestId.takeIf { it != OrderRequestId.NONE }?.asString(),
        requestOrder = orderRequest.takeIf { it != orderNone }?.toLog(),
        responseOrder = orderResponse.takeIf { it != orderNone }?.toLog(),
        responseOrders = ordersResponse.takeIf { it.isNotEmpty() }?.filter { it != orderNone }?.map { it.toLog() },
        requestFilter = orderFilterRequest.takeIf { it != OrderFilter() }?.toLog(),
    ).takeIf { it != OrderLogModel() }
}

private fun OrderFilter.toLog() = OrderFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    operationType = operationType.takeIf { it != OrderSide.NONE }?.name,
)

private fun OrderError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun Order.toLog() = OrderLog(
    id = id.takeIf { it != OrderId.NONE }?.asString(),
    secCode = secCode.takeIf { it.isNotBlank() },
    agreementNumber = agreementNumber.takeIf { it.isNotBlank() },
    quantity = quantity.takeIf { it != BigDecimal.ZERO },
    price = price.takeIf { it != BigDecimal.ZERO },
    userId = userId.takeIf { it != OrderUserId.NONE }?.asString(),
    operationType = operationType.takeIf { it != OrderSide.NONE }?.name,
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)
