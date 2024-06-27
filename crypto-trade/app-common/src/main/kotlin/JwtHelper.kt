package ru.otus.otuskotlin.crypto.trade.app.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.otus.otuskotlin.crypto.trade.common.models.OrderUserId
import ru.otus.otuskotlin.crypto.trade.common.permissions.PrincipalModel
import ru.otus.otuskotlin.crypto.trade.common.permissions.UserGroups
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

const val AUTH_HEADER: String = "x-jwt-payload"

@OptIn(ExperimentalEncodingApi::class)
fun String?.jwt2principal(): PrincipalModel = this?.let { jwtHeader ->
    val jwtJson = Base64.decode(jwtHeader).decodeToString()
    println("JWT JSON PAYLOAD: $jwtJson")
    val jwtObj = jsMapper.decodeFromString(JwtPayload.serializer(), jwtJson)
    jwtObj.toPrincipal()
}
    ?: run {
        println("No jwt found in headers")
        PrincipalModel.NONE
    }

@OptIn(ExperimentalEncodingApi::class)
fun PrincipalModel.createJwtTestHeader(): String {
    val jwtObj = fromPrincipal()
    val jwtJson = jsMapper.encodeToString(JwtPayload.serializer(), jwtObj)
    return Base64.encode(jwtJson.encodeToByteArray())
}

private val jsMapper = Json {
    ignoreUnknownKeys = true
}

@Serializable
private data class JwtPayload(
    val aud: List<String>? = null,
    val sub: String? = null,
    @SerialName("family_name")
    val familyName: String? = null,
    @SerialName("given_name")
    val givenName: String? = null,
    @SerialName("middle_name")
    val middleName: String? = null,
    val groups: List<String>? = null,
)

private fun JwtPayload.toPrincipal(): PrincipalModel = PrincipalModel(
    id = sub?.let { OrderUserId(it) } ?: OrderUserId.NONE,
    fname = givenName ?: "",
    mname = middleName ?: "",
    lname = familyName ?: "",
    groups = groups?.mapNotNull { it.toPrincipalGroup() }?.toSet() ?: emptySet(),
)

private fun PrincipalModel.fromPrincipal(): JwtPayload = JwtPayload(
    sub = id.takeIf { it != OrderUserId.NONE }?.asString(),
    givenName = fname.takeIf { it.isNotBlank() },
    middleName = mname.takeIf { it.isNotBlank() },
    familyName = lname.takeIf { it.isNotBlank() },
    groups = groups.mapNotNull { it.fromPrincipalGroup() }.toList().takeIf { it.isNotEmpty() } ?: emptyList(),
)

private fun String?.toPrincipalGroup(): UserGroups? = when (this?.uppercase()) {
    "USER" -> UserGroups.USER
    "ADMIN_ORDER" -> UserGroups.ADMIN_ORDER
    "TEST" -> UserGroups.TEST
    // TODO сделать обработку ошибок
    else -> null
}

private fun UserGroups?.fromPrincipalGroup(): String? = when (this) {
    UserGroups.USER -> "USER"
    UserGroups.ADMIN_ORDER -> "ADMIN_ORDER"
    UserGroups.TEST -> "TEST"
    // TODO сделать обработку ошибок
    else -> null
}
