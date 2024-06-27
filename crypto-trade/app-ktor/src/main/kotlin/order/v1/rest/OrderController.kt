package ru.otus.otuskotlin.crypto.trade.app.order.v1.rest

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.crypto.trade.api.v1.models.*
import ru.otus.otuskotlin.crypto.trade.app.AppSettings
import ru.otus.otuskotlin.crypto.trade.app.common.AUTH_HEADER
import ru.otus.otuskotlin.crypto.trade.app.common.controllerHelper
import ru.otus.otuskotlin.crypto.trade.app.common.jwt2principal
import ru.otus.otuskotlin.crypto.trade.mappers.fromTransport
import ru.otus.otuskotlin.crypto.trade.mappers.toTransportOrder

suspend fun ApplicationCall.createOrder(appSettings: AppSettings) =
    processOrder<OrderCreateRequest, OrderCreateResponse>(appSettings, "orderV1Create")

suspend fun ApplicationCall.readOrder(appSettings: AppSettings) =
    processOrder<OrderReadRequest, OrderReadResponse>(appSettings, "orderV1Read")

suspend fun ApplicationCall.updateOrder(appSettings: AppSettings) =
    processOrder<OrderUpdateRequest, OrderUpdateResponse>(appSettings, "orderV1Update")

suspend fun ApplicationCall.deleteOrder(appSettings: AppSettings) =
    processOrder<OrderDeleteRequest, OrderDeleteResponse>(appSettings, "orderV1Delete")

suspend fun ApplicationCall.searchOrder(appSettings: AppSettings) =
    processOrder<OrderSearchRequest, OrderSearchResponse>(appSettings, "orderV1Search")

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processOrder(
    appSettings: AppSettings,
    logId: String,
) = appSettings.controllerHelper(
    {
        principal = this@processOrder.request.header(AUTH_HEADER).jwt2principal()
        fromTransport(this@processOrder.receive<Q>())
    },
    { this@processOrder.respond(toTransportOrder() as R) },
    logId,
)