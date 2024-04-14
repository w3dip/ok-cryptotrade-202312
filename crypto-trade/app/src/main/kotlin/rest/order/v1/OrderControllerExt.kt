package ru.otus.otuskotlin.crypto.trade.app.rest.order.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.crypto.trade.api.v1.models.IRequest
import ru.otus.otuskotlin.crypto.trade.api.v1.models.IResponse
import ru.otus.otuskotlin.crypto.trade.app.AppSettings
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand
import ru.otus.otuskotlin.crypto.trade.common.models.OrderState
import ru.otus.otuskotlin.crypto.trade.log.api.v1.toLog
import ru.otus.otuskotlin.crypto.trade.mappers.fromTransport
import ru.otus.otuskotlin.crypto.trade.mappers.toTransportOrder
import ru.otus.otuskotlin.marketplace.common.helpers.asOrderError
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processOrder(
    appSettings: AppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    { fromTransport(this@processOrder.receive<Q>()) },
    { this@processOrder.respond(toTransportOrder()) },
    clazz,
    logId,
)

suspend inline fun <T> AppSettings.controllerHelper(
    crossinline getRequest: suspend OrderContext.() -> Unit,
    crossinline toResponse: suspend OrderContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = OrderContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId),
            e = e,
        )
        ctx.state = OrderState.FAILING
        ctx.errors.add(e.asOrderError())
        processor.exec(ctx)
        if (ctx.command == OrderCommand.NONE) {
            ctx.command = OrderCommand.READ
        }
        ctx.toResponse()
    }
}
