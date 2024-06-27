package ru.otus.otuskotlin.crypto.trade.core.repo

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.helpers.errorSystem
import ru.otus.otuskotlin.crypto.trade.common.helpers.fail
import ru.otus.otuskotlin.crypto.trade.common.models.OrderWorkMode
import ru.otus.otuskotlin.crypto.trade.common.permissions.UserGroups
import ru.otus.otuskotlin.crypto.trade.common.repo.IRepoOrder
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker
import ru.otus.otuskotlin.crypto.trade.core.exceptions.OrderDbNotConfiguredException

fun CorChainDsl<OrderContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы        
    """.trimIndent()
    handle {
        orderRepo = when {
            workMode == OrderWorkMode.TEST -> corSettings.repoTest
            workMode == OrderWorkMode.STUB -> corSettings.repoStub
            principal.groups.contains(UserGroups.TEST) -> corSettings.repoTest
            else -> corSettings.repoProd
        }
        if (workMode != OrderWorkMode.STUB && orderRepo == IRepoOrder.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = OrderDbNotConfiguredException(workMode)
            )
        )
    }
}
