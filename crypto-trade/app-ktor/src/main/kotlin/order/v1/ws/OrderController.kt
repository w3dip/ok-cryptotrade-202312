package ru.otus.otuskotlin.crypto.trade.app.order.v1.ws

import apiV1Mapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import ru.otus.otuskotlin.crypto.trade.api.v1.models.IRequest
import ru.otus.otuskotlin.crypto.trade.app.AppSettings
import ru.otus.otuskotlin.crypto.trade.app.common.controllerHelper
import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand
import ru.otus.otuskotlin.crypto.trade.mappers.fromTransport
import ru.otus.otuskotlin.crypto.trade.mappers.toTransportInit
import ru.otus.otuskotlin.crypto.trade.mappers.toTransportOrder

suspend fun WebSocketSession.orderHandler(appSettings: AppSettings) = with(OrderWsSession(this)) {
    val sessions = appSettings.corSettings.wsSessions
    sessions.add(this)

    // Handle init request
    appSettings.controllerHelper(
        {
            command = OrderCommand.INIT
            wsSession = this@with
        },
        { outgoing.send(Frame.Text(apiV1Mapper.writeValueAsString(toTransportInit()))) },
        "orderV1WsInit"
    )

    // Handle flow
    incoming.receiveAsFlow().mapNotNull {
        val frame = it as? Frame.Text ?: return@mapNotNull
        // Handle without flow destruction
        try {
            appSettings.controllerHelper(
                {
                    fromTransport(apiV1Mapper.readValue<IRequest>(frame.readText()))
                    wsSession = this@with
                },
                {
                    val result = apiV1Mapper.writeValueAsString(toTransportOrder())
                    // If change request, response is sent to everyone
                    outgoing.send(Frame.Text(result))
                },
                "orderV1WsHandle"
            )

        } catch (_: ClosedReceiveChannelException) {
            sessions.remove(this@with)
        } finally {
            // Handle finish request
            appSettings.controllerHelper(
                {
                    command = OrderCommand.FINISH
                    wsSession = this@with
                },
                { },
                "orderV1WsFinish"
            )
            sessions.remove(this@with)
        }
    }.collect()
}
