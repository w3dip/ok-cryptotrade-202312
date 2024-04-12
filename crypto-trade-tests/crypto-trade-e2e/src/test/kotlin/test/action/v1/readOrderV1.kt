package ru.otus.otuskotlin.crypto.trade.e2e.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldNotBe
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderReadObject
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderReadRequest
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderReadResponse
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderResponseObject
import ru.otus.otuskotlin.crypto.trade.e2e.fixture.client.Client
import ru.otus.otuskotlin.crypto.trade.e2e.test.action.beValidId

suspend fun Client.readOrder(id: String?): OrderResponseObject = readOrder(id) {
    it should haveSuccessResult
    it.order shouldNotBe null
    it.order!!
}

suspend fun <T> Client.readOrder(id: String?, block: (OrderReadResponse) -> T): T =
    withClue("readOrderV1: $id") {
        id should beValidId

        val response = sendAndReceive(
            "order/read",
            OrderReadRequest(
                requestType = "read",
                debug = debug,
                order = OrderReadObject(id = id)
            )
        ) as OrderReadResponse

        response.asClue(block)
    }
