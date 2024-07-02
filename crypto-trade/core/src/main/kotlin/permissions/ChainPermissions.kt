package ru.otus.otuskotlin.crypto.trade.core.permissions

import ru.otus.otuskotlin.crypto.trade.auth.resolveChainPermissions
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker

fun CorChainDsl<OrderContext>.chainPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление прав доступа для групп пользователей"

    on { state == OrderState.RUNNING }

    handle {
        permissionsChain.addAll(resolveChainPermissions(principal.groups))
        println("PRINCIPAL: $principal")
        println("PERMISSIONS: $permissionsChain")
    }
}
