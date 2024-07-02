package ru.otus.otuskotlin.crypto.trade.common.permissions

import ru.otus.otuskotlin.crypto.trade.common.models.OrderUserId

data class PrincipalModel(
    val id: OrderUserId = OrderUserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<UserGroups> = emptySet()
) {
    fun genericName() = "$fname $mname $lname"

    companion object {
        val NONE = PrincipalModel()
    }
}
