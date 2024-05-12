package ru.otus.otuskotlin.crypto.trade.core.general

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.chain

fun CorChainDsl<OrderContext>.operation(
    title: String,
    command: OrderCommand,
    block: CorChainDsl<OrderContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == OrderState.RUNNING }
}
