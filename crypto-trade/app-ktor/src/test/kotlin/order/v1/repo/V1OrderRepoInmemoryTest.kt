package ru.otus.otuskotlin.marketplace.app.ktor.repo

import CorSettings
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderRequestDebugMode
import ru.otus.otuskotlin.crypto.trade.app.AppSettings
import ru.otus.otuskotlin.crypto.trade.common.repo.IRepoOrder
import ru.otus.otuskotlin.crypto.trade.repo.common.OrderRepoInitialized
import ru.otus.otuskotlin.crypto.trade.repo.inmemory.OrderRepoInMemory

class V1OrderRepoInmemoryTest : V1OrderRepoBaseTest() {
    override val workMode: OrderRequestDebugMode = OrderRequestDebugMode.TEST
    private fun getAppSettings(repo: IRepoOrder) = AppSettings(
        corSettings = CorSettings(
            repoTest = repo
        )
    )

    override val appSettingsCreate: AppSettings = getAppSettings(
        repo = OrderRepoInitialized(OrderRepoInMemory(randomUuid = { uuidNew }))
    )
    override val appSettingsRead: AppSettings = getAppSettings(
        repo = OrderRepoInitialized(
            OrderRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initOrder),
        )
    )
    override val appSettingsUpdate: AppSettings = getAppSettings(
        repo = OrderRepoInitialized(
            OrderRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initOrder),
        )
    )
    override val appSettingsDelete: AppSettings = getAppSettings(
        repo = OrderRepoInitialized(
            OrderRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initOrder),
        )
    )
    override val appSettingsSearch: AppSettings = getAppSettings(
        repo = OrderRepoInitialized(
            OrderRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initOrder),
        )
    )
}
