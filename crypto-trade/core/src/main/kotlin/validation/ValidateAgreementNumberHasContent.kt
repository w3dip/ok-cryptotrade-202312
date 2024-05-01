package ru.otus.otuskotlin.crypto.trade.core.validation

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.helpers.errorValidation
import ru.otus.otuskotlin.crypto.trade.common.helpers.fail
import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.worker

fun CorChainDsl<OrderContext>.validateAgreementNumberHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { orderValidating.agreementNumber.isNotEmpty() && !orderValidating.agreementNumber.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "agreementNumber",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}
