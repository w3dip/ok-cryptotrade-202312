import ru.otus.otuskotlin.crypto.trade.common.repo.IRepoOrder
import ru.otus.otuskotlin.crypto.trade.common.ws.WsSessionRepository
import ru.otus.otuskotlin.crypto.trade.logging.common.LoggerProvider

data class CorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val wsSessions: WsSessionRepository = WsSessionRepository.NONE,
    val repoStub: IRepoOrder = IRepoOrder.NONE,
    val repoTest: IRepoOrder = IRepoOrder.NONE,
    val repoProd: IRepoOrder = IRepoOrder.NONE,
) {
    companion object {
        val NONE = CorSettings()
    }
}
