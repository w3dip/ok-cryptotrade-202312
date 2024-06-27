package ru.otus.otuskotlin.crypto.trade.e2e.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import ru.otus.otuskotlin.crypto.trade.api.v1.models.*
import ru.otus.otuskotlin.crypto.trade.e2e.fixture.client.Client
import ru.otus.otuskotlin.crypto.trade.e2e.test.action.beValidId
import ru.otus.otuskotlin.crypto.trade.e2e.test.action.beValidLock

suspend fun Client.deleteOrder(order: OrderResponseObject, debug: OrderDebug = debugStubV1) {
    val id = order.id
    val lock = order.lock
    withClue("deleteOrderV1: $id, lock: $lock") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "order/delete",
            OrderDeleteRequest(
                debug = debug,
                order = OrderDeleteObject(id = id, lock = lock)
            )
        ) as OrderDeleteResponse

        response.asClue {
            response should haveSuccessResult
            response.order shouldBe order
        }
    }
}
