package ru.otus.otuskotlin.crypto.trade.repo.cassandra

import com.benasher44.uuid.uuid4
import org.testcontainers.containers.CassandraContainer
import ru.otus.otuskotlin.crypto.trade.repo.common.OrderRepoInitialized
import ru.otus.otuskotlin.crypto.trade.repo.tests.*
import java.time.Duration

class OrderCassandraRepoCreateTest : OrderRepoCreateTest() {
    override val repo = OrderRepoInitialized(
        initObjects = initObjects,
        repo = TestCompanion.repository("ks_create", lockNew.asString())
    )
}

class OrderCassandraRepoReadTest : OrderRepoReadTest() {
    override val repo = OrderRepoInitialized(
        initObjects = initObjects,
        repo = TestCompanion.repository("ks_read")
    )
}

class OrderCassandraRepoUpdateTest : OrderRepoUpdateTest() {
    override val repo = OrderRepoInitialized(
        initObjects = initObjects,
        repo = TestCompanion.repository("ks_update", lockNew.asString())
    )
}

class OrderCassandraRepoDeleteTest : OrderRepoDeleteTest() {
    override val repo = OrderRepoInitialized(
        initObjects = initObjects,
        repo = TestCompanion.repository("ks_delete")
    )
}

class OrderCassandraRepoSearchTest : OrderRepoSearchTest() {
    override val repo = OrderRepoInitialized(
        initObjects = initObjects,
        repo = TestCompanion.repository("ks_search")
    )
}

class TestCasandraContainer : CassandraContainer<TestCasandraContainer>("cassandra:3.11.2")

object TestCompanion {
    private val container by lazy {
        TestCasandraContainer().withStartupTimeout(Duration.ofSeconds(300L))
            .also { it.start() }
    }

    fun repository(keyspace: String, uuid: String? = null): OrderRepoCassandra {
        return OrderRepoCassandra(
            keyspaceName = keyspace,
            host = container.host,
            port = container.getMappedPort(CassandraContainer.CQL_PORT),
            testing = true,
            randomUuid = uuid?.let { { uuid } } ?: { uuid4().toString() },
        )
    }
}
