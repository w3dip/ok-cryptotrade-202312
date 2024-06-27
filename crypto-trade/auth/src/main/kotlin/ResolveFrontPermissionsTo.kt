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
    UserPermissions.READ_GROUP to mapOf(
        PrincipalRelations.GROUP to OrderPermissionClient.READ
    ),
    UserPermissions.READ_PUBLIC to mapOf(
        PrincipalRelations.PUBLIC to OrderPermissionClient.READ
    ),
    UserPermissions.READ_CANDIDATE to mapOf(
        PrincipalRelations.MODERATABLE to OrderPermissionClient.READ
    ),

    // UPDATE
    UserPermissions.UPDATE_OWN to mapOf(
        PrincipalRelations.OWN to OrderPermissionClient.UPDATE
    ),
    UserPermissions.UPDATE_PUBLIC to mapOf(
        PrincipalRelations.MODERATABLE to OrderPermissionClient.UPDATE
    ),
    UserPermissions.UPDATE_CANDIDATE to mapOf(
        PrincipalRelations.MODERATABLE to OrderPermissionClient.UPDATE
    ),

    // DELETE
    UserPermissions.DELETE_OWN to mapOf(
        PrincipalRelations.OWN to OrderPermissionClient.DELETE
    ),
    UserPermissions.DELETE_PUBLIC to mapOf(
        PrincipalRelations.MODERATABLE to OrderPermissionClient.DELETE
    ),
    UserPermissions.DELETE_CANDIDATE to mapOf(
        PrincipalRelations.MODERATABLE to OrderPermissionClient.DELETE
    ),
)
