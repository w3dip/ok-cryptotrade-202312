package ru.otus.otuskotlin.crypto.trade.mappers.exceptions

class UnknownRequestClass(clazz: Class<*>) : RuntimeException("Class $clazz cannot be mapped to OrderContext")
