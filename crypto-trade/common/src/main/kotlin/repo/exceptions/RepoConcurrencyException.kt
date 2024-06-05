package ru.otus.otuskotlin.crypto.trade.common.repo.exceptions

import ru.otus.otuskotlin.crypto.trade.common.models.OrderId
import ru.otus.otuskotlin.crypto.trade.common.models.OrderLock

class RepoConcurrencyException(id: OrderId, expectedLock: OrderLock, actualLock: OrderLock?) : RepoOrderException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
