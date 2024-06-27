package ru.otus.otuskotlin.marketplace.app.ktor.auth

import io.ktor.client.request.*
import ru.otus.otuskotlin.crypto.trade.app.common.AUTH_HEADER
import ru.otus.otuskotlin.crypto.trade.app.common.createJwtTestHeader
import ru.otus.otuskotlin.crypto.trade.common.models.OrderUserId
import ru.otus.otuskotlin.crypto.trade.common.permissions.PrincipalModel
import ru.otus.otuskotlin.crypto.trade.common.permissions.UserGroups
import ru.otus.otuskotlin.crypto.trade.stubs.OrderStub.ORDER_BUY

fun HttpRequestBuilder.addAuth(principal: PrincipalModel) {
    header(AUTH_HEADER, principal.createJwtTestHeader())
}

fun HttpRequestBuilder.addAuth(
    id: OrderUserId = ORDER_BUY.userId,
    groups: Collection<UserGroups> = listOf(UserGroups.TEST, UserGroups.USER),
) {
    addAuth(PrincipalModel(id, groups = groups.toSet()))
}
