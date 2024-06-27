package ru.otus.otuskotlin.crypto.trade.auth

import ru.otus.otuskotlin.crypto.trade.common.models.Order
import ru.otus.otuskotlin.crypto.trade.common.models.OrderId
import ru.otus.otuskotlin.crypto.trade.common.permissions.PrincipalModel
import ru.otus.otuskotlin.crypto.trade.common.permissions.PrincipalRelations

fun Order.resolveRelationsTo(principal: PrincipalModel): Set<PrincipalRelations> = setOfNotNull(
    PrincipalRelations.NONE,
    // Используется при создании новой заявки
    PrincipalRelations.NEW.takeIf { id == OrderId.NONE },
    PrincipalRelations.OWN.takeIf { userId == principal.id },
)
