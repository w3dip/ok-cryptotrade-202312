package ru.otus.otuskotlin.crypto.trade.repo.cassandra

import com.benasher44.uuid.uuid4
import org.testcontainers.containers.CassandraContainer
import ru.otus.otuskotlin.crypto.trade.repo.common.OrderRepoInitialized
import ru.otus.otuskotlin.crypto.trade.repo.tests.*
import java.time.Duration

class RepoOrderCassandraCreateTest : RepoOrderCreateTest() {
    override val repo = OrderRepoInitialized(
        initObjects = initObjects,
        repo = TestCompanion.repository("ks_create", lockNew.asString())
    )
}

class RepoOrderCassandraReadTest : RepoOrderReadTest() {
    override val repo = OrderRepoInitialized(
        initObjects = initObjects,
        repo = TestCompanion.repository("ks_read")
    )
}

class RepoOrderCassandraUpdateTest : RepoOrderUpdateTest() {
    override val repo = OrderRepoInitialized(
        initObjects = initObjects,
        repo = TestCompanion.repository("ks_update", lockNew.asString())
    )
}

class RepoOrderCassandraDeleteTest : RepoOrderDeleteTest() {
    override val repo = OrderRepoInitialized(
        initObjects = initObjects,
        repo = TestCompanion.repository("ks_delete")
    )
}

class RepoOrderCassandraSearchTest : RepoOrderSearchTest() {
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

    fun repository(keyspace: String, uuid: String? = null): RepoOrderCassandra {
        return RepoOrderCassandra(
            keyspaceName = keyspace,
            host = container.host,
            port = container.getMappedPort(CassandraContainer.CQL_PORT),
            testing = true,
            randomUuid = uuid?.let { { uuid } } ?: { uuid4().toString() },
        )
    }
}
