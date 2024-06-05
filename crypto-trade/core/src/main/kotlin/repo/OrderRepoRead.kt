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

fun CorChainDsl<OrderContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение заявки из БД"
    on { state == OrderState.RUNNING }
    handle {
        val request = DbOrderIdRequest(orderValidated)
        when (val result = orderRepo.readOrder(request)) {
            is DbOrderResponseOk -> orderRepoRead = result.data
            is DbOrderResponseErr -> fail(result.errors)
            is DbOrderResponseErrWithData -> {
                fail(result.errors)
                orderRepoRead = result.data
            }
        }
    }
}
