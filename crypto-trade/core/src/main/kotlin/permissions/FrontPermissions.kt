package ru.otus.otuskotlin.crypto.trade.core.permissions

import ru.otus.otuskotlin.crypto.trade.auth.resolveFrontPermissions
import ru.otus.otuskotlin.crypto.trade.auth.resolveRelationsTo
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker

fun CorChainDsl<OrderContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == OrderState.RUNNING }

    handle {
        orderRepoDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissionsChain,
                // Повторно вычисляем отношения, поскольку они могли измениться при выполении операции
                orderRepoDone.resolveRelationsTo(principal)
            )
        )

        for (order in ordersRepoDone) {
            order.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    order.resolveRelationsTo(principal)
                )
            )
        }
    }
}
