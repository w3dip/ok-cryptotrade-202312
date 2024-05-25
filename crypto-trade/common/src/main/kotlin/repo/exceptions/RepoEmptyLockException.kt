package ru.otus.otuskotlin.crypto.trade.common.repo.exceptions

import ru.otus.otuskotlin.crypto.trade.common.models.OrderId

class RepoEmptyLockException(id: OrderId) : RepoOrderException(
    id,
    "Lock is empty in DB"
)
