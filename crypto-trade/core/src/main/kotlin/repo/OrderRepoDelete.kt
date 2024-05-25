package ru.otus.otuskotlin.crypto.trade.core.repo

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.helpers.fail
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderIdRequest
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseErr
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseErrWithData
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker

fun CorChainDsl<OrderContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == OrderState.RUNNING }
    handle {
        val request = DbOrderIdRequest(orderRepoPrepare)
        when (val result = orderRepo.deleteOrder(request)) {
            is DbOrderResponseOk -> orderRepoDone = result.data
            is DbOrderResponseErr -> {
                fail(result.errors)
                orderRepoDone = orderRepoRead
            }

            is DbOrderResponseErrWithData -> {
                fail(result.errors)
                orderRepoDone = result.data
            }
        }
    }
}
