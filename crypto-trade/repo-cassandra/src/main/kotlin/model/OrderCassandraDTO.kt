package ru.otus.otuskotlin.crypto.trade.repo.cassandra.model

import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import ru.otus.otuskotlin.crypto.trade.common.models.Order
import ru.otus.otuskotlin.crypto.trade.common.models.OrderId
import ru.otus.otuskotlin.crypto.trade.common.models.OrderLock
import ru.otus.otuskotlin.crypto.trade.common.models.OrderUserId
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

@Entity
data class OrderCassandraDTO(
    @field:CqlName(COLUMN_ID)
    @field:PartitionKey
    var id: String? = null,

    @field:CqlName(COLUMN_SEC_CODE)
    var secCode: String? = null,

    @field:CqlName(COLUMN_AGREEMENT_NUMBER)
    var agreementNumber: String? = null,

    @field:CqlName(COLUMN_QUANTITY)
    var quantity: String? = null,

    @field:CqlName(COLUMN_PRICE)
    var price: String? = null,

    @field:CqlName(COLUMN_USER_ID)
    var userId: String? = null,

    @field:CqlName(COLUMN_OPERATION_TYPE)
    var operationType: OrderSide? = null,

    @field:CqlName(COLUMN_LOCK)
    var lock: String?,
) {
    constructor(orderModel: Order) : this(
        id = orderModel.id.takeIf { it != OrderId.NONE }?.asString(),
        secCode = orderModel.secCode.takeIf { it.isNotBlank() },
        agreementNumber = orderModel.agreementNumber.takeIf { it.isNotBlank() },
        quantity = orderModel.quantity.takeIf { it != ZERO }?.toPlainString(),
        price = orderModel.price.takeIf { it != ZERO }?.toPlainString(),
        userId = orderModel.userId.takeIf { it != OrderUserId.NONE }?.asString(),
        operationType = orderModel.operationType.toTransport(),
        lock = orderModel.lock.takeIf { it != OrderLock.NONE }?.asString()
    )

    fun toOrderModel(): Order =
        Order(
            id = id?.let { OrderId(it) } ?: OrderId.NONE,
            secCode = secCode ?: "",
            agreementNumber = agreementNumber ?: "",
            quantity = quantity?.let { BigDecimal(it) } ?: ZERO,
            price = price?.let { BigDecimal(it) } ?: ZERO,
            userId = userId?.let { OrderUserId(it) } ?: OrderUserId.NONE,
            operationType = operationType.fromTransport(),
            lock = lock?.let { OrderLock(it) } ?: OrderLock.NONE
        )

    companion object {
        const val TABLE_NAME = "orders"

        const val COLUMN_ID = "id"
        const val COLUMN_SEC_CODE = "secCode"
        const val COLUMN_AGREEMENT_NUMBER = "agreementNumber"
        const val COLUMN_QUANTITY = "quantity"
        const val COLUMN_PRICE = "price"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_OPERATION_TYPE = "operation_type"
        const val COLUMN_LOCK = "lock"

        fun table(keyspace: String, tableName: String) =
            SchemaBuilder
                .createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey(COLUMN_ID, DataTypes.TEXT)
                .withColumn(COLUMN_SEC_CODE, DataTypes.TEXT)
                .withColumn(COLUMN_AGREEMENT_NUMBER, DataTypes.TEXT)
                .withColumn(COLUMN_QUANTITY, DataTypes.TEXT)
                .withColumn(COLUMN_PRICE, DataTypes.TEXT)
                .withColumn(COLUMN_USER_ID, DataTypes.TEXT)
                .withColumn(COLUMN_OPERATION_TYPE, DataTypes.TEXT)
                .withColumn(COLUMN_LOCK, DataTypes.TEXT)
                .build()

        fun secCodeIndex(keyspace: String, tableName: String, locale: String = "en") =
            SchemaBuilder
                .createIndex()
                .ifNotExists()
                .usingSASI()
                .onTable(keyspace, tableName)
                .andColumn(COLUMN_SEC_CODE)
                .withSASIOptions(mapOf("mode" to "CONTAINS", "tokenization_locale" to locale))
                .build()
    }
}
