package ru.otus.otuskotlin.crypto.trade.common.models

@JvmInline
value class OrderId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = OrderId("")
    }
}
