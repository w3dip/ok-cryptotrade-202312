package ru.otus.otuskotlin.crypto.trade.mappers

import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderCreateObject
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderDeleteObject
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderReadObject
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderUpdateObject
import ru.otus.otuskotlin.crypto.trade.common.models.Order
import ru.otus.otuskotlin.crypto.trade.common.models.OrderId
import ru.otus.otuskotlin.crypto.trade.common.models.OrderLock
import java.math.BigDecimal.ZERO

fun Order.toTransportCreate() = OrderCreateObject(
    secCode = secCode.takeIf { it.isNotBlank() },
    agreementNumber = agreementNumber.takeIf { it.isNotBlank() },
    quantity = quantity.takeIf { it != ZERO },
    price = price.takeIf { it != ZERO },
    operationType = operationType.toTransportOrder()
)

fun Order.toTransportRead() = OrderReadObject(
    id = id.takeIf { it != OrderId.NONE }?.asString(),
)

fun Order.toTransportUpdate() = OrderUpdateObject(
    id = id.takeIf { it != OrderId.NONE }?.asString(),
    secCode = secCode.takeIf { it.isNotBlank() },
    agreementNumber = agreementNumber.takeIf { it.isNotBlank() },
    quantity = quantity.takeIf { it != ZERO },
    price = price.takeIf { it != ZERO },
    operationType = operationType.toTransportOrder(),
    lock = lock.takeIf { it != OrderLock.NONE }?.asString(),
)

fun Order.toTransportDelete() = OrderDeleteObject(
    id = id.takeIf { it != OrderId.NONE }?.asString(),
    lock = lock.takeIf { it != OrderLock.NONE }?.asString(),
)
