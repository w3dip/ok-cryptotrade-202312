package ru.otus.otuskotlin.crypto.trade.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.repo.*
import ru.otus.otuskotlin.crypto.trade.common.repo.exceptions.RepoEmptyLockException
import ru.otus.otuskotlin.crypto.trade.repo.common.OrderRepoInitializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class OrderRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : OrderRepoBase(), IRepoOrder, OrderRepoInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, OrderEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(orders: Collection<Order>) = orders.map { order ->
        val entity = OrderEntity(order)
        require(entity.id != null)
        cache.put(entity.id, entity)
        order
    }

    override suspend fun createOrder(rq: DbOrderRequest): IDbOrderResponse = tryOrderMethod {
        val key = randomUuid()
        val order = rq.order.copy(id = OrderId(key), lock = OrderLock(randomUuid()))
        val entity = OrderEntity(order)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbOrderResponseOk(order)
    }

    override suspend fun readOrder(rq: DbOrderIdRequest): IDbOrderResponse = tryOrderMethod {
        val key = rq.id.takeIf { it != OrderId.NONE }?.asString() ?: return@tryOrderMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbOrderResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateOrder(rq: DbOrderRequest): IDbOrderResponse = tryOrderMethod {
        val rqOrder = rq.order
        val id = rqOrder.id.takeIf { it != OrderId.NONE } ?: return@tryOrderMethod errorEmptyId
        val key = id.asString()
        val oldLock = rqOrder.lock.takeIf { it != OrderLock.NONE } ?: return@tryOrderMethod errorEmptyLock(id)

        mutex.withLock {
            val oldOrder = cache.get(key)?.toInternal()
            when {
                oldOrder == null -> errorNotFound(id)
                oldOrder.lock == OrderLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldOrder.lock != oldLock -> errorRepoConcurrency(oldOrder, oldLock)
                else -> {
                    val newOrder = rqOrder.copy(lock = OrderLock(randomUuid()))
                    val entity = OrderEntity(newOrder)
                    cache.put(key, entity)
                    DbOrderResponseOk(newOrder)
                }
            }
        }
    }


    override suspend fun deleteOrder(rq: DbOrderIdRequest): IDbOrderResponse = tryOrderMethod {
        val id = rq.id.takeIf { it != OrderId.NONE } ?: return@tryOrderMethod errorEmptyId
        val key = id.asString()
        val oldLock = rq.lock.takeIf { it != OrderLock.NONE } ?: return@tryOrderMethod errorEmptyLock(id)

        mutex.withLock {
            val oldOrder = cache.get(key)?.toInternal()
            when {
                oldOrder == null -> errorNotFound(id)
                oldOrder.lock == OrderLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldOrder.lock != oldLock -> errorRepoConcurrency(oldOrder, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbOrderResponseOk(oldOrder)
                }
            }
        }
    }

    /**
     * Поиск заявок по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchOrder(rq: DbOrderFilterRequest): IDbOrdersResponse = tryOrdersMethod {
        val result: List<Order> = cache.asMap().asSequence()
            .filter { entry ->
                rq.userId.takeIf { it != OrderUserId.NONE }?.let {
                    it.asString() == entry.value.userId
                } ?: true
            }
            .filter { entry ->
                rq.operationType.takeIf { it != OrderSide.NONE }?.let {
                    it.name == entry.value.operationType
                } ?: true
            }
            .filter { entry ->
                rq.secCodeFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.secCode?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbOrdersResponseOk(result)
    }
}
