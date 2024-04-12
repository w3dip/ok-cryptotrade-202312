package ru.otus.otuskotlin.crypto.trade.e2e.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderResponseObject
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderUpdateObject
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderUpdateRequest
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderUpdateResponse
import ru.otus.otuskotlin.crypto.trade.e2e.fixture.client.Client
import ru.otus.otuskotlin.crypto.trade.e2e.test.action.beValidId
import ru.otus.otuskotlin.crypto.trade.e2e.test.action.beValidLock

suspend fun Client.updateOrder(order: OrderUpdateObject): OrderResponseObject =
    updateOrder(order) {
        it should haveSuccessResult
        it.order shouldNotBe null
        it.order?.apply {
            if (order.secCode != null)
                secCode shouldBe order.secCode
            if (order.agreementNumber != null)
                agreementNumber shouldBe order.agreementNumber
            if (order.quantity != null)
                quantity shouldBe order.quantity
            if (order.price != null)
                price shouldBe order.price
            if (order.operationType != null)
                operationType shouldBe order.operationType
        }
        it.order!!
    }

suspend fun <T> Client.updateOrder(order: OrderUpdateObject, block: (OrderUpdateResponse) -> T): T {
    val id = order.id
    val lock = order.lock
    return withClue("updatedV1: $id, lock: $lock, set: $order") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "order/update", OrderUpdateRequest(
                debug = debug,
                order = order.copy(id = id, lock = lock)
            )
        ) as OrderUpdateResponse

        response.asClue(block)
    }
}
