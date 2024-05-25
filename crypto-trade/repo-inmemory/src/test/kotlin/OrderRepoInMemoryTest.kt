package ru.otus.otuskotlin.crypto.trade.repo.inmemory

import ru.otus.otuskotlin.crypto.trade.repo.common.OrderRepoInitialized
import ru.otus.otuskotlin.crypto.trade.repo.tests.*

class OrderRepoInMemoryCreateTest : RepoOrderCreateTest() {
    override val repo = OrderRepoInitialized(
        OrderRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}

class OrderRepoInMemoryDeleteTest : RepoOrderDeleteTest() {
    override val repo = OrderRepoInitialized(
        OrderRepoInMemory(),
        initObjects = initObjects,
    )
}

class OrderRepoInMemoryReadTest : RepoOrderReadTest() {
    override val repo = OrderRepoInitialized(
        OrderRepoInMemory(),
        initObjects = initObjects,
    )
}

class OrderRepoInMemorySearchTest : RepoOrderSearchTest() {
    override val repo = OrderRepoInitialized(
        OrderRepoInMemory(),
        initObjects = initObjects,
    )
}

class OrderRepoInMemoryUpdateTest : RepoOrderUpdateTest() {
    override val repo = OrderRepoInitialized(
        OrderRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}
