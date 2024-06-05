package ru.otus.otuskotlin.crypto.trade.common.repo

interface IRepoOrder {
    suspend fun createOrder(rq: DbOrderRequest): IDbOrderResponse
    suspend fun readOrder(rq: DbOrderIdRequest): IDbOrderResponse
    suspend fun updateOrder(rq: DbOrderRequest): IDbOrderResponse
    suspend fun deleteOrder(rq: DbOrderIdRequest): IDbOrderResponse
    suspend fun searchOrder(rq: DbOrderFilterRequest): IDbOrdersResponse

    companion object {
        val NONE = object : IRepoOrder {
            override suspend fun createOrder(rq: DbOrderRequest): IDbOrderResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readOrder(rq: DbOrderIdRequest): IDbOrderResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateOrder(rq: DbOrderRequest): IDbOrderResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteOrder(rq: DbOrderIdRequest): IDbOrderResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchOrder(rq: DbOrderFilterRequest): IDbOrdersResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
