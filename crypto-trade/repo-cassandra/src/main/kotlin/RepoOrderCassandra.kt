package ru.otus.otuskotlin.crypto.trade.repo.cassandra

import com.benasher44.uuid.uuid4
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.internal.core.type.codec.extras.enums.EnumNameCodec
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import ru.otus.otuskotlin.crypto.trade.common.models.Order
import ru.otus.otuskotlin.crypto.trade.common.models.OrderId
import ru.otus.otuskotlin.crypto.trade.common.models.OrderLock
import ru.otus.otuskotlin.crypto.trade.common.repo.*
import ru.otus.otuskotlin.crypto.trade.repo.cassandra.model.OrderCassandraDTO
import ru.otus.otuskotlin.crypto.trade.repo.cassandra.model.OrderSide
import ru.otus.otuskotlin.crypto.trade.repo.common.IRepoOrderInitializable
import java.net.InetAddress
import java.net.InetSocketAddress
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RepoOrderCassandra(
    private val keyspaceName: String,
    private val host: String = "",
    private val port: Int = 9042,
    private val user: String = "cassandra",
    private val pass: String = "cassandra",
    private val testing: Boolean = false,
    private val timeout: Duration = 30.toDuration(DurationUnit.SECONDS),
    private val randomUuid: () -> String = { uuid4().toString() },
    initObjects: Collection<Order> = emptyList(),
) : OrderRepoBase(), IRepoOrder, IRepoOrderInitializable {
    private val codecRegistry by lazy {
        DefaultCodecRegistry("default").apply {
            register(EnumNameCodec(OrderSide::class.java))
        }
    }

    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(parseAddresses(host, port))
            .withLocalDatacenter("datacenter1")
            .withAuthCredentials(user, pass)
            .withCodecRegistry(codecRegistry)
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    private fun createSchema(keyspace: String) {
        session.execute(
            SchemaBuilder
                .createKeyspace(keyspace)
                .ifNotExists()
                .withSimpleStrategy(1)
                .build()
        )
        session.execute(OrderCassandraDTO.table(keyspace, OrderCassandraDTO.TABLE_NAME))
        session.execute(OrderCassandraDTO.secCodeIndex(keyspace, OrderCassandraDTO.TABLE_NAME))
    }

    private val dao by lazy {
        if (testing) {
            createSchema(keyspaceName)
        }
        mapper.orderDao(keyspaceName, OrderCassandraDTO.TABLE_NAME).apply {
            runBlocking {
                initObjects.map { model ->
                    withTimeout(timeout) {
                        create(OrderCassandraDTO(model)).await()
                    }
                }
            }
        }
    }

    override fun save(orders: Collection<Order>): Collection<Order> =
        orders.onEach { dao.create(OrderCassandraDTO(it)) }

    override suspend fun createOrder(rq: DbOrderRequest): IDbOrderResponse = tryOrderMethod {
        val new = rq.order.copy(id = OrderId(randomUuid()), lock = OrderLock(randomUuid()))
        dao.create(OrderCassandraDTO(new))
        DbOrderResponseOk(new)
    }

    override suspend fun readOrder(rq: DbOrderIdRequest): IDbOrderResponse = tryOrderMethod {
        if (rq.id == OrderId.NONE) return@tryOrderMethod errorEmptyId
        val res = dao.read(rq.id.asString()).await() ?: return@tryOrderMethod errorNotFound(rq.id)
        DbOrderResponseOk(res.toOrderModel())
    }

    override suspend fun updateOrder(rq: DbOrderRequest): IDbOrderResponse = tryOrderMethod {
        val idStr = rq.order.id.asString()
        val prevLock = rq.order.lock.asString()
        val new = rq.order.copy(lock = OrderLock(randomUuid()))
        val dto = OrderCassandraDTO(new)

        val res = dao.update(dto, prevLock).await()
        val isSuccess = res.wasApplied()
        val resultField = res.one()
            ?.takeIf { it.columnDefinitions.contains(OrderCassandraDTO.COLUMN_LOCK) }
            ?.getString(OrderCassandraDTO.COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }
        when {
            // Два варианта почти эквивалентны, выбирайте который вам больше подходит
            isSuccess -> DbOrderResponseOk(new)
            // res.wasApplied() -> DbAdResponse.success(dao.read(idStr).await()?.toAdModel())
            resultField == null -> errorNotFound(rq.order.id)
            else -> errorRepoConcurrency(
                oldOrder = dao.read(idStr).await()?.toOrderModel() ?: throw Exception(
                    "Consistency DB problem: Object with ID $idStr and requested lock $prevLock " +
                            "was denied for update but the same object was not found in db at further request"
                ),
                expectedLock = rq.order.lock
            )
        }
    }

    override suspend fun deleteOrder(rq: DbOrderIdRequest): IDbOrderResponse = tryOrderMethod {
        val idStr = rq.id.asString()
        val prevLock = rq.lock.asString()
        val oldOrder = dao.read(idStr).await()?.toOrderModel() ?: return@tryOrderMethod errorNotFound(rq.id)
        val res = dao.delete(idStr, prevLock).await()
        val isSuccess = res.wasApplied()
        val resultField = res.one()
            ?.takeIf { it.columnDefinitions.contains(OrderCassandraDTO.COLUMN_LOCK) }
            ?.getString(OrderCassandraDTO.COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }
        when {
            isSuccess -> DbOrderResponseOk(oldOrder)
            resultField == null -> errorNotFound(rq.id)
            else -> errorRepoConcurrency(
                dao.read(idStr).await()?.toOrderModel() ?: throw Exception(
                    "Consistency DB problem: Object with ID $idStr and requested lock $prevLock " +
                            "was successfully read but was denied for delete"
                ),
                rq.lock
            )
        }
    }

    override suspend fun searchOrder(rq: DbOrderFilterRequest): IDbOrdersResponse = tryOrdersMethod {
        val found = dao.search(rq).await()
        DbOrdersResponseOk(found.map { it.toOrderModel() })
    }

    private fun parseAddresses(hosts: String, port: Int): Collection<InetSocketAddress> = hosts
        .split(Regex("""\s*,\s*"""))
        .map { InetSocketAddress(InetAddress.getByName(it), port) }
}
