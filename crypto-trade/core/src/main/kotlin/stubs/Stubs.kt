package ru.otus.otuskotlin.crypto.trade.core.stubs

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.common.models.OrderWorkMode
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.chain

fun CorChainDsl<OrderContext>.stubs(title: String, block: CorChainDsl<OrderContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == OrderWorkMode.STUB && state == OrderState.RUNNING }
}
