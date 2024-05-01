import ru.otus.otuskotlin.crypto.trade.common.ws.WsSessionRepository
import ru.otus.otuskotlin.crypto.trade.logging.common.LoggerProvider

data class CorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val wsSessions: WsSessionRepository = WsSessionRepository.NONE,
) {
    companion object {
        val NONE = CorSettings()
    }
}
