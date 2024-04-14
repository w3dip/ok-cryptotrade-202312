package ru.otus.otuskotlin.marketplace.app.kafka

import apiV1RequestSerialize
import apiV1ResponseDeserialize
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import ru.otus.otuskotlin.crypto.trade.api.v1.models.*
import ru.otus.otuskotlin.crypto.trade.app.kafka.AppKafkaConfig
import ru.otus.otuskotlin.crypto.trade.app.kafka.AppKafkaConsumer
import ru.otus.otuskotlin.crypto.trade.app.kafka.ConsumerStrategyV1
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertEquals

class KafkaControllerTest {

    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        OrderCreateRequest(
                            order = OrderCreateObject(
                                secCode = "BTC",
                                agreementNumber = "A001",
                                quantity = BigDecimal.valueOf(5),
                                price = BigDecimal.valueOf(65000),
                                operationType = OrderSide.BUY
                            ),
                            debug = OrderDebug(
                                mode = OrderRequestDebugMode.STUB,
                                stub = OrderRequestDebugStubs.SUCCESS
                            )
                        )
                    )
                )
            )
            app.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<OrderCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("BTC", result.order?.secCode)
    }

    companion object {
        const val PARTITION = 0
    }
}


