package ru.otus.otuskotlin.crypto.trade.common.models

import java.math.BigDecimal
import java.math.BigDecimal.ZERO

data class Order(
    var id: OrderId = OrderId.NONE,
    var secCode: String = "",
    var agreementNumber: String = "",
    var quantity: BigDecimal = ZERO,
    var price: BigDecimal = ZERO,
    var userId: OrderUserId = OrderUserId.NONE,
    var operationType: OrderSide = OrderSide.NONE,
    var lock: OrderLock = OrderLock.NONE,
    val permissionsClient: MutableSet<OrderPermissionClient> = mutableSetOf()
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = Order()
    }

}
