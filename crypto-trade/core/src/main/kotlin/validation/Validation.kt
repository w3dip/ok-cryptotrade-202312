package ru.otus.otuskotlin.crypto.trade.core.validation

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.chain

fun CorChainDsl<OrderContext>.validation(block: CorChainDsl<OrderContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == OrderState.RUNNING }
}
