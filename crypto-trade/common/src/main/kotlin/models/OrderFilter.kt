package ru.otus.otuskotlin.crypto.trade.common.models

data class OrderFilter(
    var searchString: String = "",
    var userId: OrderUserId = OrderUserId.NONE,
    var operationType: OrderSide = OrderSide.NONE,
) {
    fun deepCopy(): OrderFilter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = OrderFilter()
    }
}
