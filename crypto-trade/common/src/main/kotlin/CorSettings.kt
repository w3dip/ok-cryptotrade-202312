import ru.otus.otuskotlin.crypto.trade.logging.common.LoggerProvider

data class CorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
) {
    companion object {
        val NONE = CorSettings()
    }
}
