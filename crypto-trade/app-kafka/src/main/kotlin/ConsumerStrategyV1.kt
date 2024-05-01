package ru.otus.otuskotlin.crypto.trade.app.kafka

import apiV1RequestDeserialize
import apiV1ResponseSerialize
import ru.otus.otuskotlin.crypto.trade.api.v1.models.IRequest
import ru.otus.otuskotlin.crypto.trade.api.v1.models.IResponse
import ru.otus.otuskotlin.crypto.trade.common.OrderContext
import ru.otus.otuskotlin.crypto.trade.mappers.fromTransport
import ru.otus.otuskotlin.crypto.trade.mappers.toTransportOrder

class ConsumerStrategyV1 : ConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: OrderContext): String {
        val response: IResponse = source.toTransportOrder()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: OrderContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}
