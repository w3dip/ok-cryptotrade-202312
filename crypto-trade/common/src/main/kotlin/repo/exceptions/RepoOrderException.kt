package ru.otus.otuskotlin.crypto.trade.common.repo.exceptions

import ru.otus.otuskotlin.crypto.trade.common.models.OrderId

open class RepoOrderException(
    @Suppress("unused")
    val orderId: OrderId,
    msg: String,
) : RepoException(msg)
