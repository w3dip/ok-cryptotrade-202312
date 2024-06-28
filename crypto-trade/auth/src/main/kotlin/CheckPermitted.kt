package ru.otus.otuskotlin.crypto.trade.auth

import ru.otus.otuskotlin.crypto.trade.common.models.OrderCommand
import ru.otus.otuskotlin.crypto.trade.common.permissions.PrincipalRelations
import ru.otus.otuskotlin.crypto.trade.common.permissions.UserPermissions

/**
 * Вычисляет доступность выполнения операции.
 * Здесь происходит сравнение доступных прав (пермишинов) и фактических отношений принципала к объекту, с которым работаем
 */
fun checkPermitted(
    command: OrderCommand,
    relations: Iterable<PrincipalRelations>,
    permissions: Iterable<UserPermissions>,
) = relations.asSequence().flatMap { relation ->
    permissions.map { permission ->
        AccessTableConditions(
            command = command,
            permission = permission,
            relation = relation,
        )
    }
}.any {
    accessTable[it] != null
}

private data class AccessTableConditions(
    val command: OrderCommand,
    val permission: UserPermissions,
    val relation: PrincipalRelations
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = OrderCommand.CREATE,
        permission = UserPermissions.CREATE_OWN,
        relation = PrincipalRelations.NEW,
    ) to true,

    // Read
    AccessTableConditions(
        command = OrderCommand.READ,
        permission = UserPermissions.READ_OWN,
        relation = PrincipalRelations.OWN,
    ) to true,

    // Update
    AccessTableConditions(
        command = OrderCommand.UPDATE,
        permission = UserPermissions.UPDATE_OWN,
        relation = PrincipalRelations.OWN,
    ) to true,

    // Delete
    AccessTableConditions(
        command = OrderCommand.DELETE,
        permission = UserPermissions.DELETE_OWN,
        relation = PrincipalRelations.OWN,
    ) to true,

    // Search
    AccessTableConditions(
        command = OrderCommand.SEARCH,
        permission = UserPermissions.SEARCH_OWN,
        relation = PrincipalRelations.OWN,
    ) to true,

    // Read
    AccessTableConditions(
        command = OrderCommand.READ,
        permission = UserPermissions.READ_ALL,
        relation = PrincipalRelations.ALL,
    ) to true,

    // Update
    AccessTableConditions(
        command = OrderCommand.UPDATE,
        permission = UserPermissions.UPDATE_ALL,
        relation = PrincipalRelations.ALL,
    ) to true,

    // Delete
    AccessTableConditions(
        command = OrderCommand.DELETE,
        permission = UserPermissions.DELETE_ALL,
        relation = PrincipalRelations.ALL,
    ) to true,

    // Search
    AccessTableConditions(
        command = OrderCommand.SEARCH,
        permission = UserPermissions.SEARCH_ALL,
        relation = PrincipalRelations.ALL,
    ) to true,
)
