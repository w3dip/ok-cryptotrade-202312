package ru.otus.otuskotlin.crypto.trade.e2e.test

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.collections.shouldExistInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderDebug
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderSearchFilter
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderUpdateObject
import ru.otus.otuskotlin.crypto.trade.e2e.fixture.client.Client
import ru.otus.otuskotlin.crypto.trade.e2e.test.action.v1.*
import java.math.BigDecimal

fun FunSpec.testApiV1(client: Client, prefix: String = "", debug: OrderDebug = debugStubV1) {
    context("${prefix}v1") {
        test("Create Order ok") {
            client.createOrder(debug = debug)
        }

        test("Read Order ok") {
            val created = client.createOrder(debug = debug)
            client.readOrder(created.id).asClue {
                it shouldBe created
            }
        }

        test("Update Order ok") {
            val created = client.createOrder(debug = debug)
            val updateOrder = OrderUpdateObject(
                id = created.id,
                lock = created.lock,
                secCode = "BTC",
                agreementNumber = "A001",
                quantity = BigDecimal.valueOf(5),
                price = BigDecimal.valueOf(65000)
            )
            client.updateOrder(updateOrder)
        }

        test("Delete Order ok") {
            val created = client.createOrder(debug = debug)
            client.deleteOrder(created)
        }

        test("Search Order ok") {
            val created1 = client.createOrder(someCreateOrder.copy(secCode = "BTC"), debug = debug)
            val created2 = client.createOrder(someCreateOrder.copy(secCode = "ETH"), debug = debug)

            withClue("Search BTC") {
                val results = client.searchOrder(search = OrderSearchFilter(searchString = "BTC"), debug = debug)
                results shouldHaveSize 1
                results shouldExist { it.secCode == created1.secCode }
            }

            withClue("Search ETH") {
                client.searchOrder(search = OrderSearchFilter(searchString = "ETH"), debug = debug)
                    .shouldExistInOrder({ it.secCode == created2.secCode })
            }
        }
    }

}
