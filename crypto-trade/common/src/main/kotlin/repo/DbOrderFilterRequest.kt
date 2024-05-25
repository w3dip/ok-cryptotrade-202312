package ru.otus.otuskotlin.crypto.trade.common.repo

import ru.otus.otuskotlin.crypto.trade.common.models.OrderSide
import ru.otus.otuskotlin.crypto.trade.common.models.OrderUserId

data class DbOrderFilterRequest(
    val secCodeFilter: String = "",
    val userId: OrderUserId = OrderUserId.NONE,
    val operationType: OrderSide = OrderSide.NONE,
)
