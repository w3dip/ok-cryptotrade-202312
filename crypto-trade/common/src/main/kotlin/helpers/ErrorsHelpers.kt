package ru.otus.otuskotlin.crypto.trade.common.helpers

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderError
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
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

inline fun OrderContext.addError(error: OrderError) = errors.add(error)
inline fun OrderContext.addErrors(error: Collection<OrderError>) = errors.addAll(error)


inline fun OrderContext.fail(error: OrderError) {
    addError(error)
    state = OrderState.FAILING
}

inline fun OrderContext.fail(errors: Collection<OrderError>) {
    addErrors(errors)
    state = OrderState.FAILING
}

inline fun errorValidation(
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

inline fun errorSystem(
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
