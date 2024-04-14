package ru.otus.otuskotlin.crypto.trade.app.rest.order.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.crypto.trade.app.AppSettings

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
    }
}