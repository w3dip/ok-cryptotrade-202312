package ru.otus.otuskotlin.crypto.trade.auth

import ru.otus.otuskotlin.crypto.trade.common.permissions.UserGroups
import ru.otus.otuskotlin.crypto.trade.common.permissions.UserPermissions

/**
 * На вход подаем группы/роли из JWT, на выход получаем пермишины, соответствующие этим группам/ролям
 */
fun resolveChainPermissions(
    groups: Iterable<UserGroups>,
) = mutableSetOf<UserPermissions>()
    .apply {
        // Группы, добавляющие права (пермишины)
        addAll(groups.flatMap { groupPermissionsAdmits[it] ?: emptySet() })
        // Группы, запрещающие права (пермишины)
        removeAll(groups.flatMap { groupPermissionsDenys[it] ?: emptySet() }.toSet())
    }
    .toSet()

private val groupPermissionsAdmits: Map<UserGroups, Set<UserPermissions>> = mapOf(
    UserGroups.USER to setOf(
        UserPermissions.READ_OWN,
        UserPermissions.READ_PUBLIC,
        UserPermissions.CREATE_OWN,
        UserPermissions.UPDATE_OWN,
        UserPermissions.DELETE_OWN,
    ),
    UserGroups.ADMIN_ORDER to setOf(),
    UserGroups.TEST to setOf(),
)

private val groupPermissionsDenys: Map<UserGroups, Set<UserPermissions>> = mapOf(
    UserGroups.USER to setOf(),
    UserGroups.ADMIN_ORDER to setOf(),
    UserGroups.TEST to setOf(),
)
