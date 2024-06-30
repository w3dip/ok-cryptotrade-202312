package ru.otus.otuskotlin.crypto.trade.common.permissions

@Suppress("unused")
enum class UserPermissions {
    CREATE_OWN,
    READ_OWN,
    UPDATE_OWN,
    DELETE_OWN,
    SEARCH_OWN,

    READ_ALL,
    DELETE_ALL,
    SEARCH_ALL
}
