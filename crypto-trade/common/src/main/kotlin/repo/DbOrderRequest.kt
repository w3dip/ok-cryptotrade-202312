package ru.otus.otuskotlin.crypto.trade.common.repo

import ru.otus.otuskotlin.crypto.trade.common.models.Order

data class DbOrderRequest(
    val order: Order
)
