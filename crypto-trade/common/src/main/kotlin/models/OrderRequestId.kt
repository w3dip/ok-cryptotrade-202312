package ru.otus.otuskotlin.crypto.trade.common.models

@JvmInline
value class OrderRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = OrderRequestId("")
    }
}
