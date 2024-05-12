package ru.otus.otuskotlin.crypto.trade.cor.handlers

import ru.otus.otuskotlin.crypto.trade.cor.CorDslMarker
import ru.otus.otuskotlin.crypto.trade.cor.CorExec
import ru.otus.otuskotlin.crypto.trade.cor.CorWorkerDsl

class CorWorker<T>(
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    private val blockHandle: suspend T.() -> Unit = {},
    blockExcept: suspend T.(Throwable) -> Unit = {},
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {
    override suspend fun handle(context: T) = blockHandle(context)
}

@CorDslMarker
class CorWorkerDslImpl<T> : AbstractCorExecDsl<T>(), CorWorkerDsl<T> {
    private var blockHandle: suspend T.() -> Unit = {}
    override fun handle(function: suspend T.() -> Unit) {
        blockHandle = function
    }

    override fun build(): CorExec<T> = CorWorker(
        title = title,
        description = description,
        blockOn = blockOn,
        blockHandle = blockHandle,
        blockExcept = blockExcept
    )

}
