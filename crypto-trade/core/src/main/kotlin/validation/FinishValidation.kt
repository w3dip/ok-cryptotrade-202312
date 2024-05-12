package ru.otus.otuskotlin.crypto.trade.core.validation

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker

fun CorChainDsl<OrderContext>.finishOrderValidation(title: String) = worker {
    this.title = title
    on { state == OrderState.RUNNING }
    handle {
        orderValidated = orderValidating
    }
}

fun CorChainDsl<OrderContext>.finishOrderFilterValidation(title: String) = worker {
    this.title = title
    on { state == OrderState.RUNNING }
    handle {
        orderFilterValidated = orderFilterValidating
    }
}
