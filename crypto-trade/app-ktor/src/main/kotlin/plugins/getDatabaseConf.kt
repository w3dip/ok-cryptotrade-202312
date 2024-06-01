package ru.otus.otuskotlin.crypto.trade.app.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.crypto.trade.app.configs.CassandraConfig
import ru.otus.otuskotlin.crypto.trade.app.configs.ConfigPaths
import ru.otus.otuskotlin.crypto.trade.common.repo.IRepoOrder
import ru.otus.otuskotlin.crypto.trade.repo.cassandra.OrderRepoCassandra
import ru.otus.otuskotlin.crypto.trade.repo.inmemory.OrderRepoInMemory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

enum class OrderDbType(val confName: String) {
    PROD("prod"), TEST("test")
}

fun Application.getDatabaseConf(type: OrderDbType): IRepoOrder {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "cassandra", "nosql", "cass" -> initCassandra()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: " +
                    "'inmemory', 'cassandra'"
        )
    }
}

fun Application.initInMemory(): IRepoOrder {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return OrderRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}

private fun Application.initCassandra(): IRepoOrder {
    val config = CassandraConfig(environment.config)
    return OrderRepoCassandra(
        keyspaceName = config.keyspace,
        host = config.host,
        port = config.port,
        user = config.user,
        pass = config.pass,
    )
}
