package ru.otus.otuskotlin.crypto.trade.e2e.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import ru.otus.otuskotlin.crypto.trade.api.v1.models.*
import ru.otus.otuskotlin.crypto.trade.e2e.fixture.client.Client

suspend fun Client.searchOrder(search: OrderSearchFilter, debug: OrderDebug = debugStubV1): List<OrderResponseObject> =
    searchOrder(search, debug = debug) {
    it should haveSuccessResult
    it.orders ?: listOf()
}

suspend fun <T> Client.searchOrder(
    search: OrderSearchFilter,
    debug: OrderDebug = debugStubV1,
    block: (OrderSearchResponse) -> T
): T =
    withClue("searchOrderV1: $search") {
        val response = sendAndReceive(
            "order/search",
            OrderSearchRequest(
                requestType = "search",
                debug = debug,
                orderFilter = search,
            )
        ) as OrderSearchResponse

        response.asClue(block)
    }
