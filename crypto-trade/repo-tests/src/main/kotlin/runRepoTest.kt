package ru.otus.otuskotlin.crypto.trade.repo.tests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes

fun runRepoTest(testBody: suspend TestScope.() -> Unit) = runTest(timeout = 2.minutes) {
    withContext(Dispatchers.Default) {
        testBody()
    }
}
