package ru.otus.otuskotlin.crypto.trade.core.repo

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.helpers.fail
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.common.permissions.SearchPermissions
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderFilterRequest
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrdersResponseErr
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrdersResponseOk
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker

fun CorChainDsl<OrderContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск заявок в БД по фильтру"
    on { state == OrderState.RUNNING }
    handle {
        val request = DbOrderFilterRequest(
            secCodeFilter = orderFilterValidated.searchString,
            operationType = orderFilterValidated.operationType,
        )
        if (orderFilterValidated.searchPermissions.contains(SearchPermissions.OWN)) {
            request.userId = principal.id
        }
        when (val result = orderRepo.searchOrder(request)) {
            is DbOrdersResponseOk -> ordersRepoDone = result.data.toMutableList()
            is DbOrdersResponseErr -> fail(result.errors)
        }
    }
}
