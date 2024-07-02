package ru.otus.otuskotlin.crypto.trade.auth

import ru.otus.otuskotlin.crypto.trade.common.models.OrderPermissionClient
import ru.otus.otuskotlin.crypto.trade.common.permissions.PrincipalRelations
import ru.otus.otuskotlin.crypto.trade.common.permissions.UserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<UserPermissions>,
    relations: Iterable<PrincipalRelations>,
) = mutableSetOf<OrderPermissionClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

/**
 * Это трехмерная таблица пермишин в бэкенде->отношение к объявлению->пермишин на фронте
 */
private val accessTable = mapOf(
    // READ
    UserPermissions.READ_OWN to mapOf(
        PrincipalRelations.OWN to OrderPermissionClient.READ
    ),

    // UPDATE
    UserPermissions.UPDATE_OWN to mapOf(
        PrincipalRelations.OWN to OrderPermissionClient.UPDATE
    ),

    // DELETE
    UserPermissions.DELETE_OWN to mapOf(
        PrincipalRelations.OWN to OrderPermissionClient.DELETE
    ),

    // SEARCH
    UserPermissions.SEARCH_OWN to mapOf(
        PrincipalRelations.OWN to OrderPermissionClient.SEARCH
    ),

    // READ
    UserPermissions.READ_ALL to mapOf(
        PrincipalRelations.ALL to OrderPermissionClient.READ
    ),

    // DELETE
    UserPermissions.DELETE_ALL to mapOf(
        PrincipalRelations.ALL to OrderPermissionClient.DELETE
    ),

    // SEARCH
    UserPermissions.SEARCH_ALL to mapOf(
        PrincipalRelations.ALL to OrderPermissionClient.SEARCH
    ),
)
