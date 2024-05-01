package ru.otus.otuskotlin.crypto.trade.app.order.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import ru.otus.otuskotlin.crypto.trade.app.AppSettings
import ru.otus.otuskotlin.crypto.trade.app.order.v1.rest.*
import ru.otus.otuskotlin.crypto.trade.app.order.v1.ws.orderHandler

fun Route.orderV1(appSettings: AppSettings) {
    route("order") {
        post("create") {
            call.createOrder(appSettings)
        }
        post("read") {
            call.readOrder(appSettings)
        }
        post("update") {
            call.updateOrder(appSettings)
        }
        post("delete") {
            call.deleteOrder(appSettings)
        }
        post("search") {
            call.searchOrder(appSettings)
        }
        webSocket("ws") {
            orderHandler(appSettings)
        }
    }
}