package ru.otus.otuskotlin.crypto.trade.repo.cassandra

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.annotations.*
import ru.otus.otuskotlin.crypto.trade.common.repo.DbOrderFilterRequest
import ru.otus.otuskotlin.crypto.trade.repo.cassandra.model.OrderCassandraDTO
import java.util.concurrent.CompletionStage

@Dao
interface OrderCassandraDAO {
    @Insert
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun create(dto: OrderCassandraDTO): CompletionStage<Unit>

    @Select
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    fun read(id: String): CompletionStage<OrderCassandraDTO?>

    @Update(customIfClause = "lock = :prevLock")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun update(dto: OrderCassandraDTO, prevLock: String): CompletionStage<AsyncResultSet>

    @Delete(
        customWhereClause = "id = :id",
        customIfClause = "lock = :prevLock",
        entityClass = [OrderCassandraDTO::class]
    )
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun delete(id: String, prevLock: String): CompletionStage<AsyncResultSet>

    @QueryProvider(providerClass = OrderCassandraSearchProvider::class, entityHelpers = [OrderCassandraDTO::class])
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    fun search(filter: DbOrderFilterRequest): CompletionStage<Collection<OrderCassandraDTO>>
}
