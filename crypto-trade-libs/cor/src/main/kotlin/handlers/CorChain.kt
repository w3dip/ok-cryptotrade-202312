package ru.otus.otuskotlin.crypto.trade.cor.handlers

import ru.otus.otuskotlin.crypto.trade.cor.CorChainDsl
import ru.otus.otuskotlin.crypto.trade.cor.CorDslMarker
import ru.otus.otuskotlin.crypto.trade.cor.CorExec
import ru.otus.otuskotlin.crypto.trade.cor.CorExecDsl

/**
 * Реализация цепочки (chain), которая исполняет свои вложенные цепочки и рабочие
 */
class CorChain<T>(
    private val execs: List<CorExec<T>>,
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(Throwable) -> Unit = {},
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {
    override suspend fun handle(context: T) {
        execs.forEach {
            it.exec(context)
        }
    }
}

@CorDslMarker
class CorChainDslImpl<T>(
) : AbstractCorExecDsl<T>(), CorChainDsl<T> {
    private val workers: MutableList<CorExecDsl<T>> = mutableListOf()
    override fun add(worker: CorExecDsl<T>) {
        workers.add(worker)
    }

    override fun build(): CorExec<T> = CorChain(
        title = title,
        description = description,
        execs = workers.map { it.build() },
        blockOn = blockOn,
        blockExcept = blockExcept
    )
}
