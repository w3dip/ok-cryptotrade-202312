package ru.otus.otuskotlin.crypto.trade.core.validation

import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand
import kotlin.test.Test

class BizValidationCreateTest : BaseBizValidationTest() {
    override val command: OrderCommand = OrderCommand.CREATE

    @Test
    fun correctSecCode() = validationSecCodeCorrect(command, processor)
    @Test
    fun trimSecCode() = validationSecCodeTrim(command, processor)
    @Test
    fun emptySecCode() = validationSecCodeEmpty(command, processor)
    @Test
    fun badSymbolsSecCode() = validationSecCodeSymbols(command, processor)

    @Test
    fun correctAgreementNumber() = validationAgreementNumberCorrect(command, processor)
    @Test
    fun trimAgreementNumber() = validationAgreementNumberTrim(command, processor)
    @Test
    fun emptyAgreementNumber() = validationAgreementNumberEmpty(command, processor)
    @Test
    fun badSymbolsAgreementNumber() = validationAgreementNumberSymbols(command, processor)

    @Test
    fun correctQuantity() = validationQuantityCorrect(command, processor)
    @Test
    fun negativeQuantity() = validationQuantityNegative(command, processor)
    @Test
    fun zeroQuantity() = validationQuantityZero(command, processor)

    @Test
    fun correctPrice() = validationPriceCorrect(command, processor)
    @Test
    fun negativePrice() = validationPriceNegative(command, processor)
    @Test
    fun zeroPrice() = validationPriceZero(command, processor)
}
