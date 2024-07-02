package ru.otus.otuskotlin.crypto.trade.app.order.v1.ws

import apiV1ResponseSerialize
import io.ktor.websocket.*
import ru.otus.otuskotlin.crypto.trade.api.v1.models.IResponse
import ru.otus.otuskotlin.crypto.trade.common.ws.WsSession

data class OrderWsSession(
    val session: WebSocketSession
) : WsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is IResponse)
        session.send(Frame.Text(apiV1ResponseSerialize(obj)))
    }
}
