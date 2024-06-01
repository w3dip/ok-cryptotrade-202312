package ru.otus.otuskotlin.crypto.trade.core.validation

import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand
import kotlin.test.Test

class ValidationDeleteTest : BaseValidationTest() {
    override val command = OrderCommand.DELETE

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun trimId() = validationIdTrim(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)
    @Test
    fun badFormatId() = validationIdFormat(command, processor)

    @Test
    fun correctLock() = validationLockCorrect(command, processor)
    @Test
    fun trimLock() = validationLockTrim(command, processor)
    @Test
    fun emptyLock() = validationLockEmpty(command, processor)
    @Test
    fun badFormatLock() = validationLockFormat(command, processor)

}
