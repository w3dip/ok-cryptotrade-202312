package ru.otus.otuskotlin.marketplace.app.ktor.repo

import CorSettings
import com.benasher44.uuid.uuid4
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.CassandraContainer
import ru.otus.otuskotlin.crypto.trade.api.v1.models.OrderRequestDebugMode
import ru.otus.otuskotlin.crypto.trade.app.AppSettings
import ru.otus.otuskotlin.crypto.trade.common.repo.IRepoOrder
import ru.otus.otuskotlin.crypto.trade.repo.cassandra.RepoOrderCassandra
import ru.otus.otuskotlin.crypto.trade.repo.common.OrderRepoInitialized
import java.time.Duration

class V1OrderRepoCassandraTest : V1OrderRepoBaseTest() {
    override val workMode: OrderRequestDebugMode = OrderRequestDebugMode.TEST
    private fun getAppSettings(repo: IRepoOrder) = AppSettings(
        corSettings = CorSettings(
            repoTest = repo
        )
    )

    override val appSettingsCreate: AppSettings = getAppSettings(
        repo = OrderRepoInitialized(repository("ks_create", uuidNew))
    )
    override val appSettingsRead: AppSettings = getAppSettings(
        repo = OrderRepoInitialized(
            repository("ks_read"),
            initObjects = listOf(initOrder),
        )
    )
    override val appSettingsUpdate: AppSettings = getAppSettings(
        repo = OrderRepoInitialized(
            repository("ks_update", uuidNew),
            initObjects = listOf(initOrder),
        )
    )
    override val appSettingsDelete: AppSettings = getAppSettings(
        repo = OrderRepoInitialized(
            repository("ks_delete"),
            initObjects = listOf(initOrder),
        )
    )
    override val appSettingsSearch: AppSettings = getAppSettings(
        repo = OrderRepoInitialized(
            repository("ks_search"),
            initObjects = listOf(initOrder),
        )
    )

    companion object {
        class TestCasandraContainer : CassandraContainer<TestCasandraContainer>("cassandra:3.11.2")

        private val container by lazy {
            @Suppress("Since15")
            TestCasandraContainer().withStartupTimeout(Duration.ofSeconds(300L))
        }

        @JvmStatic
        @BeforeClass
        fun tearUp() {
            container.start()
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            container.stop()
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
}
