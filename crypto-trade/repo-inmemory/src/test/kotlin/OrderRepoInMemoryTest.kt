package ru.otus.otuskotlin.crypto.trade.repo.inmemory

import ru.otus.otuskotlin.crypto.trade.repo.common.OrderRepoInitialized
import ru.otus.otuskotlin.crypto.trade.repo.tests.*

class OrderInMemoryRepoCreateTest : OrderRepoCreateTest() {
    override val repo = OrderRepoInitialized(
        OrderRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}

class OrderInMemoryRepoDeleteTest : OrderRepoDeleteTest() {
    override val repo = OrderRepoInitialized(
        OrderRepoInMemory(),
        initObjects = initObjects,
    )
}

class OrderInMemoryRepoReadTest : OrderRepoReadTest() {
    override val repo = OrderRepoInitialized(
        OrderRepoInMemory(),
        initObjects = initObjects,
    )
}

class OrderInMemoryRepoSearchTest : OrderRepoSearchTest() {
    override val repo = OrderRepoInitialized(
        OrderRepoInMemory(),
        initObjects = initObjects,
    )
}

class OrderInMemoryRepoUpdateTest : OrderRepoUpdateTest() {
    override val repo = OrderRepoInitialized(
        OrderRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}
