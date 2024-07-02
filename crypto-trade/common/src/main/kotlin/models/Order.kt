package ru.otus.otuskotlin.crypto.trade.common.models

import ru.otus.otuskotlin.crypto.trade.common.permissions.PrincipalRelations
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

data class Order(
    var id: OrderId = OrderId.NONE,
    var secCode: String = "",
    var agreementNumber: String = "",
    var quantity: BigDecimal = ZERO,
    var price: BigDecimal = ZERO,
    var userId: OrderUserId = OrderUserId.NONE,
    var operationType: OrderSide = OrderSide.NONE,
    var lock: OrderLock = OrderLock.NONE,
    // Результат вычисления отношений текущего пользователя (который сделал запрос) к текущей заявке
    var principalRelations: Set<PrincipalRelations> = emptySet(),
    // Набор пермишинов, которые отдадим во фронтенд
    val permissionsClient: MutableSet<OrderPermissionClient> = mutableSetOf()
) {
    fun deepCopy(): Order = copy(
        permissionsClient = permissionsClient.toMutableSet(),
    )
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = Order()
    }

}
