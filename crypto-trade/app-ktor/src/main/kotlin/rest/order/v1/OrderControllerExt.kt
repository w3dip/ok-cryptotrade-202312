package ru.otus.otuskotlin.crypto.trade.app.rest.order.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.crypto.trade.api.v1.models.IRequest
import ru.otus.otuskotlin.crypto.trade.api.v1.models.IResponse
import ru.otus.otuskotlin.crypto.trade.app.AppSettings
import ru.otus.otuskotlin.crypto.trade.app.common.controllerHelper
import ru.otus.otuskotlin.crypto.trade.mappers.fromTransport
import ru.otus.otuskotlin.crypto.trade.mappers.toTransportOrder
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