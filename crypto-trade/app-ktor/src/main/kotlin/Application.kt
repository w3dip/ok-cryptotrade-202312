package ru.otus.otuskotlin.crypto.trade.app

import CorSettings
import apiV1Mapper
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.slf4j.event.Level
import ru.otus.otuskotlin.crypto.trade.app.order.v1.orderV1Rest
import ru.otus.otuskotlin.crypto.trade.app.order.v1.orderV1Ws
import ru.otus.otuskotlin.crypto.trade.core.OrderProcessor
import ru.otus.otuskotlin.crypto.trade.logging.common.LoggerProvider
import ru.otus.otuskotlin.crypto.trade.logging.logback.loggerLogback

fun Application.module(
    appSettings: AppSettings = initAppSettings()
) {
    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(CallLogging) {
        level = Level.INFO
    }
    install(WebSockets)
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
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }
            orderV1Rest(appSettings)
            orderV1Ws(appSettings)
        }
    }
}

fun Application.initAppSettings(): AppSettings {
    val corSettings = CorSettings(
        loggerProvider = getLoggerProviderConf(),
    )
    return AppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = OrderProcessor(corSettings),
    )
}

fun Application.getLoggerProviderConf(): LoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "logback", null -> LoggerProvider { loggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Additted value is logback (default)")
    }
