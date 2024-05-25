package ru.otus.otuskotlin.crypto.trade.repo.common

import ru.otus.otuskotlin.crypto.trade.common.models.Order
import ru.otus.otuskotlin.crypto.trade.common.repo.IRepoOrder

interface IRepoOrderInitializable : IRepoOrder {
    fun save(orders: Collection<Order>): Collection<Order>
}
