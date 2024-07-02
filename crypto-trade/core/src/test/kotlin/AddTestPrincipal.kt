package ru.otus.otuskotlin.marketplace.biz

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderUserId
import ru.otus.otuskotlin.crypto.trade.common.permissions.PrincipalModel
import ru.otus.otuskotlin.crypto.trade.common.permissions.UserGroups
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub.ORDER_BUY

fun OrderContext.addTestPrincipal(userId: OrderUserId = ORDER_BUY.userId) {
    principal = PrincipalModel(
        id = userId,
        groups = setOf(
            UserGroups.USER,
            UserGroups.TEST,
        )
    )
}
