package ru.otus.otuskotlin.crypto.trade.common.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import ru.otus.otuskotlin.crypto.trade.common.helpers.errorSystem
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

abstract class OrderRepoBase : IRepoOrder {

    protected suspend fun tryOrderMethod(
        timeout: Duration = 10.seconds,
        ctx: CoroutineContext = Dispatchers.IO,
        block: suspend () -> IDbOrderResponse
    ) = try {
        withTimeout(timeout) {
            withContext(ctx) {
                block()
            }
        }
    } catch (e: Throwable) {
        DbOrderResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryOrdersMethod(block: suspend () -> IDbOrdersResponse) = try {
        block()
    } catch (e: Throwable) {
        DbOrdersResponseErr(errorSystem("methodException", e = e))
    }

}
