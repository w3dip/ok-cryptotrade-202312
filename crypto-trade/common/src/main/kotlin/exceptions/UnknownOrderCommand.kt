package ru.otus.otuskotlin.crypto.trade.common.exceptions

import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand

class UnknownOrderCommand(command: OrderCommand) : Throwable("Wrong command $command at mapping toTransport stage")
