package ru.otus.otuskotlin.crypto.trade.common.models

import ru.otus.otuskotlin.crypto.trade.common.permissions.SearchPermissions

data class OrderFilter(
    var searchString: String = "",
    var operationType: OrderSide = OrderSide.NONE,
    var searchPermissions: MutableSet<SearchPermissions> = mutableSetOf(),
) {
    fun deepCopy(): OrderFilter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = OrderFilter()
    }
}
