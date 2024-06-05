package ru.otus.otuskotlin.crypto.trade.repo.inmemory

import ru.otus.otuskotlin.crypto.trade.common.models.*
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

data class OrderEntity(
    val id: String? = null,
    val secCode: String? = null,
    val agreementNumber: String? = null,
    var quantity: BigDecimal? = null,
    var price: BigDecimal? = null,
    val userId: String? = null,
    val operationType: String? = null,
    val lock: String? = null,
) {
    constructor(model: Order) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        secCode = model.secCode.takeIf { it.isNotBlank() },
        agreementNumber = model.agreementNumber.takeIf { it.isNotBlank() },
        quantity = model.quantity.takeIf { it != ZERO },
        price = model.price.takeIf { it != ZERO },
        userId = model.userId.asString().takeIf { it.isNotBlank() },
        operationType = model.operationType.takeIf { it != OrderSide.NONE }?.name,
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = Order(
        id = id?.let { OrderId(it) } ?: OrderId.NONE,
        secCode = secCode ?: "",
        agreementNumber = agreementNumber ?: "",
        quantity = quantity ?: ZERO,
        price = price ?: ZERO,
        userId = userId?.let { OrderUserId(it) } ?: OrderUserId.NONE,
        operationType = operationType?.let { OrderSide.valueOf(it) } ?: OrderSide.NONE,
        lock = lock?.let { OrderLock(it) } ?: OrderLock.NONE,
    )
}
