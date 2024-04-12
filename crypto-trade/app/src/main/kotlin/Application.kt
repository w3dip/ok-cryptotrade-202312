package ru.otus.otuskotlin.crypto.trade.app

import apiV1Mapper
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor

fun Application.module(
    appSettings: AppSettings = initAppSettings()
) {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        /* TODO
            Это временное решение, оно опасно.
            В боевом приложении здесь должны быть конкретные настройки
        */
        allowCredentials = true
        anyHost()
    }

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("v1") {
            install(ContentNegotiation) {
                json(apiV1Mapper)
            }
            v2Ad(appSettings)
            webSocket("/ws") {
                wsHandlerV2(appSettings)
            }
        }
    }
}

fun Application.initAppSettings(): AppSettings {
    return AppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        processor = OrderProcessor(),
    )
}
