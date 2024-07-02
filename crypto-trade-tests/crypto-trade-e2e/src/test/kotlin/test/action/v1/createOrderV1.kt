package ru.otus.otuskotlin.crypto.trade.e2e.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import ru.otus.otuskotlin.crypto.trade.api.v1.models.*
import ru.otus.otuskotlin.crypto.trade.e2e.fixture.client.Client

suspend fun Client.createOrder(
    order: OrderCreateObject = someCreateOrder,
    debug: OrderDebug = debugStubV1
): OrderResponseObject = createOrder(order) {
    it should haveSuccessResult
    it.order shouldNotBe null
    it.order?.apply {
        secCode shouldBe order.secCode
        agreementNumber shouldBe order.agreementNumber
        quantity shouldBe order.quantity
        price shouldBe order.price
        operationType shouldBe order.operationType
    }
    it.order!!
}

suspend fun <T> Client.createOrder(
    order: OrderCreateObject = someCreateOrder,
    debug: OrderDebug = debugStubV1,
    block: (OrderCreateResponse) -> T
): T =
    withClue("createOrderV1: $order") {
        val response = sendAndReceive(
            "order/create", OrderCreateRequest(
                requestType = "create",
                debug = debug,
                order = order
            )
        ) as OrderCreateResponse

        response.asClue(block)
    }
