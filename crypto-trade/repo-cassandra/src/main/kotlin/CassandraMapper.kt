package ru.otus.otuskotlin.crypto.trade.repo.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace
import com.datastax.oss.driver.api.mapper.annotations.DaoTable
import com.datastax.oss.driver.api.mapper.annotations.Mapper

@Mapper
interface CassandraMapper {
    @DaoFactory
    fun orderDao(@DaoKeyspace keyspace: String, @DaoTable tableName: String): OrderCassandraDAO

    companion object {
        fun builder(session: CqlSession) = CassandraMapperBuilder(session)
    }
}