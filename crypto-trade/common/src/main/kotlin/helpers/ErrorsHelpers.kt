package ru.otus.otuskotlin.crypto.trade.common.helpers

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand
import ru.otus.otuskotlin.crypto.trade.common.models.OrderError
import ru.otus.otuskotlin.crypto.trade.common.models.OrderId
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.common.permissions.PrincipalModel
import ru.otus.otuskotlin.crypto.trade.logging.common.LogLevel

fun Throwable.asOrderError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = OrderError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun OrderContext.addError(error: OrderError) = errors.add(error)
fun OrderContext.addErrors(error: Collection<OrderError>) = errors.addAll(error)


fun OrderContext.fail(error: OrderError) {
    addError(error)
    state = OrderState.FAILING
}

fun OrderContext.fail(errors: Collection<OrderError>) {
    addErrors(errors)
    state = OrderState.FAILING
}

fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = OrderError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = OrderError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)

fun accessViolation(
    principal: PrincipalModel,
    operation: OrderCommand,
    orderId: OrderId = OrderId.NONE,
) = OrderError(
    code = "access-${operation.name.lowercase()}",
    group = "access",
    message = "User ${principal.genericName()} (${principal.id.asString()}) is not allowed to perform operation ${operation.name}"
            + if (orderId != OrderId.NONE) " on order ${orderId.asString()}" else "",
    level = LogLevel.ERROR,
)
