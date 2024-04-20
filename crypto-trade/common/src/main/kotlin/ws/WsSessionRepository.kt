package ru.otus.otuskotlin.crypto.trade.common.ws

interface WsSessionRepository {
    fun add(session: WsSession)
    fun clearAll()
    fun remove(session: WsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : WsSessionRepository {
            override fun add(session: WsSession) {}
            override fun clearAll() {}
            override fun remove(session: WsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}
