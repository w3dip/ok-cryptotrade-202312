package ru.otus.otuskotlin.crypto.trade.repo.common

import ru.otus.otuskotlin.crypto.trade.common.models.Order

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class OrderRepoInitialized(
    val repo: OrderRepoInitializable,
    initObjects: Collection<Order> = emptyList(),
) : OrderRepoInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<Order> = save(initObjects).toList()
}
