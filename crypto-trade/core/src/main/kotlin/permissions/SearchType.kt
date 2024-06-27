package ru.otus.otuskotlin.crypto.trade.core.permissions

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.common.permissions.SearchPermissions
import ru.otus.otuskotlin.crypto.trade.common.permissions.UserPermissions
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.chain
import ru.otus.otuskotlin.crypto.trade.cor.worker

fun CorChainDsl<OrderContext>.searchTypes(title: String) = chain {
    this.title = title
    description = "Добавление ограничений в поисковый запрос согласно правам доступа и др. политикам"
    on { state == OrderState.RUNNING }
    worker("Определение типа поиска") {
        orderFilterValidated.searchPermissions = setOfNotNull(
            SearchPermissions.OWN.takeIf { permissionsChain.contains(UserPermissions.SEARCH_OWN) },
            SearchPermissions.PUBLIC.takeIf { permissionsChain.contains(UserPermissions.SEARCH_PUBLIC) },
            SearchPermissions.REGISTERED.takeIf { permissionsChain.contains(UserPermissions.SEARCH_REGISTERED) },
        ).toMutableSet()
    }
}
