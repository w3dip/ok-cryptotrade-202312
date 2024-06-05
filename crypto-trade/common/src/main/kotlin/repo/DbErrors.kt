package ru.otus.otuskotlin.crypto.trade.common.repo

import ru.otus.otuskotlin.crypto.trade.common.helpers.errorSystem
import ru.otus.otuskotlin.crypto.trade.common.models.Order
import ru.otus.otuskotlin.crypto.trade.common.models.OrderError
import ru.otus.otuskotlin.crypto.trade.common.models.OrderId
import ru.otus.otuskotlin.crypto.trade.common.models.OrderLock
import ru.otus.otuskotlin.crypto.trade.common.repo.exceptions.RepoConcurrencyException
import ru.otus.otuskotlin.crypto.trade.common.repo.exceptions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: OrderId) = DbOrderResponseErr(
    OrderError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbOrderResponseErr(
    OrderError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldOrder: Order,
    expectedLock: OrderLock,
    exception: Exception = RepoConcurrencyException(
        id = oldOrder.id,
        expectedLock = expectedLock,
        actualLock = oldOrder.lock,
    ),
) = DbOrderResponseErrWithData(
    order = oldOrder,
    err = OrderError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldOrder.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: OrderId) = DbOrderResponseErr(
    OrderError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Order ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbOrderResponseErr(
    errorSystem(
        violationCode = "db-error",
        e = e
    )
)
