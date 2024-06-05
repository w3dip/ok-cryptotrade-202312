package ru.otus.otuskotlin.crypto.trade.core.repo

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker

fun CorChainDsl<OrderContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == OrderState.RUNNING }
    handle {
        orderRepoPrepare = orderRepoRead.deepCopy().apply {
            secCode = orderValidated.secCode
            agreementNumber = orderValidated.agreementNumber
            operationType = orderValidated.operationType
            quantity = orderValidated.quantity
            price = orderValidated.price
            lock = orderValidated.lock
        }
    }
}
