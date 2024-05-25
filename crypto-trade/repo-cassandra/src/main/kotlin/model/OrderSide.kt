package ru.otus.otuskotlin.crypto.trade.repo.cassandra.model

import ru.otus.otuskotlin.crypto.trade.common.models.OrderSide
import ru.otus.otuskotlin.crypto.trade.repo.cassandra.model.OrderSide.BUY
import ru.otus.otuskotlin.crypto.trade.repo.cassandra.model.OrderSide.SELL

enum class OrderSide {
    BUY,
    SELL,
}

fun ru.otus.otuskotlin.crypto.trade.repo.cassandra.model.OrderSide?.fromTransport() = when (this) {
    null -> OrderSide.NONE
    BUY -> OrderSide.BUY
    SELL -> OrderSide.SELL
}

fun OrderSide.toTransport() = when (this) {
    OrderSide.NONE -> null
    OrderSide.BUY -> BUY
    OrderSide.SELL -> SELL
}

