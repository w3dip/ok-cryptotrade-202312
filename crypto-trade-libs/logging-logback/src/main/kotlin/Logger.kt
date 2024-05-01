package ru.otus.otuskotlin.crypto.trade.logging.logback

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.crypto.trade.logging.common.LogWrapper
import kotlin.reflect.KClass

/**
 * Generate internal LogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun loggerLogback(logger: Logger): LogWrapper = LogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun loggerLogback(clazz: KClass<*>): LogWrapper = loggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)

@Suppress("unused")
fun loggerLogback(loggerId: String): LogWrapper = loggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
