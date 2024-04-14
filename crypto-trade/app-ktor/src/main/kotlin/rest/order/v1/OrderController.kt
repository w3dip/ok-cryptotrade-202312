package ru.otus.otuskotlin.crypto.trade.app.rest.order.v1

import io.ktor.server.application.*
import ru.otus.otuskotlin.crypto.trade.api.v1.models.*
import ru.otus.otuskotlin.crypto.trade.app.AppSettings
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createOrder::class
suspend fun ApplicationCall.createOrder(appSettings: AppSettings) =
    processOrder<OrderCreateRequest, OrderCreateResponse>(appSettings, clCreate, "create")

val clRead: KClass<*> = ApplicationCall::readOrder::class
suspend fun ApplicationCall.readOrder(appSettings: AppSettings) =
    processOrder<OrderReadRequest, OrderReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::updateOrder::class
suspend fun ApplicationCall.updateOrder(appSettings: AppSettings) =
    processOrder<OrderUpdateRequest, OrderUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::deleteOrder::class
suspend fun ApplicationCall.deleteOrder(appSettings: AppSettings) =
    processOrder<OrderDeleteRequest, OrderDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::searchOrder::class
suspend fun ApplicationCall.searchOrder(appSettings: AppSettings) =
    processOrder<OrderSearchRequest, OrderSearchResponse>(appSettings, clSearch, "search")
