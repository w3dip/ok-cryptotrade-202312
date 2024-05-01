package ru.otus.otuskotlin.crypto.trade.core.validation

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.helpers.errorValidation
import ru.otus.otuskotlin.crypto.trade.common.helpers.fail
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker

fun CorChainDsl<OrderContext>.validateAgreementNumberNotEmpty(title: String) = worker {
    this.title = title
    on { orderValidating.agreementNumber.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "agreementNumber",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
