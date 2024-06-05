package ru.otus.otuskotlin.crypto.trade.core.exceptions

import ru.otus.otuskotlin.crypto.trade.common.models.OrderWorkMode

class OrderDbNotConfiguredException(val workMode: OrderWorkMode) : Exception(
    "Database is not configured properly for workmode $workMode"
)
