package ru.otus.otuskotlin.crypto.trade.repo.cassandra

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.MapperContext
import com.datastax.oss.driver.api.mapper.entity.EntityHelper
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import ru.otus.otuskotlin.crypto.trade.common.models.OrderSide
import ru.otus.otuskotlin.crypto.trade.common.models.OrderUserId
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderFilterRequest
import ru.otus.otuskotlin.crypto.trade.repo.cassandra.model.OrderCassandraDTO
import ru.otus.otuskotlin.crypto.trade.repo.cassandra.model.toTransport
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.function.BiConsumer

class OrderCassandraSearchProvider(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<OrderCassandraDTO>
) {
    fun search(filter: DbOrderFilterRequest): CompletionStage<Collection<OrderCassandraDTO>> {
        var select = entityHelper.selectStart().allowFiltering()

        if (filter.secCodeFilter.isNotBlank()) {
            select = select
                .whereColumn(OrderCassandraDTO.COLUMN_SEC_CODE)
                .like(QueryBuilder.literal("%${filter.secCodeFilter}%"))
        }
        if (filter.userId != OrderUserId.NONE) {
            select = select
                .whereColumn(OrderCassandraDTO.COLUMN_USER_ID)
                .isEqualTo(QueryBuilder.literal(filter.userId.asString(), context.session.context.codecRegistry))
        }
        if (filter.operationType != OrderSide.NONE) {
            select = select
                .whereColumn(OrderCassandraDTO.COLUMN_OPERATION_TYPE)
                .isEqualTo(
                    QueryBuilder.literal(
                        filter.operationType.toTransport(),
                        context.session.context.codecRegistry
                    )
                )
        }

        val asyncFetcher = AsyncFetcher()

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }

    inner class AsyncFetcher : BiConsumer<AsyncResultSet?, Throwable?> {
        private val buffer = mutableListOf<OrderCassandraDTO>()
        private val future = CompletableFuture<Collection<OrderCassandraDTO>>()
        val stage: CompletionStage<Collection<OrderCassandraDTO>> = future

        override fun accept(resultSet: AsyncResultSet?, t: Throwable?) {
            when {
                t != null -> future.completeExceptionally(t)
                resultSet == null -> future.completeExceptionally(IllegalStateException("ResultSet should not be null"))
                else -> {
                    buffer.addAll(resultSet.currentPage().map { entityHelper.get(it, false) })
                    if (resultSet.hasMorePages())
                        resultSet.fetchNextPage().whenComplete(this)
                    else
                        future.complete(buffer)
                }
            }
        }
    }
}