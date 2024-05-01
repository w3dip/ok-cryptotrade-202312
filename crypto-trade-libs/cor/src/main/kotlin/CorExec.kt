package ru.otus.otuskotlin.crypto.trade.cor

/**
 * Блок кода, который обрабатывает контекст. Имеет имя и описание
 */
interface CorExec<T> {
    val title: String
    val description: String
    suspend fun exec(context: T)
}
