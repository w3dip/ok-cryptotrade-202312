package ru.otus.otuskotlin.crypto.trade.core.repo

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.helpers.fail
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderRequest
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseErr
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseErrWithData
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker

fun CorChainDsl<OrderContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == OrderState.RUNNING }
    handle {
        val request = DbOrderRequest(orderRepoPrepare)
        when (val result = orderRepo.updateOrder(request)) {
            is DbOrderResponseOk -> orderRepoDone = result.data
            is DbOrderResponseErr -> fail(result.errors)
            is DbOrderResponseErrWithData -> {
                fail(result.errors)
                orderRepoDone = result.data
            }
        }
    }
}
