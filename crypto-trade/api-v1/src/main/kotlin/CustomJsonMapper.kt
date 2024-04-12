import kotlinx.serialization.json.Json
import ru.otus.otuskotlin.crypto.trade.api.v1.models.IRequest
import ru.otus.otuskotlin.crypto.trade.api.v1.models.IResponse

val apiV1Mapper = Json

@Suppress("unused")
fun apiV1RequestSerialize(request: IRequest): String = apiV1Mapper.encodeToString(IRequest.serializer())

@Suppress("UNCHECKED_CAST", "unused")
fun <T : IRequest> apiV1RequestDeserialize(json: String): T =
    apiV1Mapper.decodeFromString<IRequest>(json) as T

@Suppress("unused")
fun apiV1ResponseSerialize(response: IResponse): String = apiV1Mapper.writeValueAsString(response)

@Suppress("UNCHECKED_CAST", "unused")
fun <T : IResponse> apiV1ResponseDeserialize(json: String): T =
    apiV1Mapper.readValue(json, IResponse::class.java) as T
