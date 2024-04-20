package ru.otus.otuskotlin.crypto.trade.app.common

import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand.NONE
import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand.READ
import ru.otus.otuskotlin.crypto.trade.log.api.v1.toLog
import ru.otus.otuskotlin.marketplace.common.helpers.asOrderError

suspend inline fun <T> CommonAppSettings.controllerHelper(
    crossinline getRequest: suspend OrderContext.() -> Unit,
    crossinline toResponse: suspend OrderContext.() -> T,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(logId)
    val ctx = OrderContext(
        timeStart = kotlinx.datetime.Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed",
            marker = "BIZ",
            data = ctx.toLog(logId),
            e = e,
        )
        ctx.state = ru.otus.otuskotlin.crypto.trade.common.models.OrderState.FAILING
        ctx.errors.add(e.asOrderError())
        processor.exec(ctx)
        if (ctx.command == NONE) {
            ctx.command = READ
        }
        ctx.toResponse()
    }
}
