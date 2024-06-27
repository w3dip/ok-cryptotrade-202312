package ru.otus.otuskotlin.crypto.trade.common

import CorSettings
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.crypto.trade.common.models.*
import ru.otus.otuskotlin.crypto.trade.common.permissions.PrincipalModel
import ru.otus.otuskotlin.crypto.trade.common.permissions.UserPermissions
import ru.otus.otuskotlin.crypto.trade.common.repo.IRepoOrder
import ru.otus.otuskotlin.crypto.trade.common.stubs.OrderStubs
import ru.otus.otuskotlin.crypto.trade.common.ws.WsSession

data class OrderContext(
    var command: OrderCommand = OrderCommand.NONE,
    var state: OrderState = OrderState.NONE,
    val errors: MutableList<OrderError> = mutableListOf(),

    var corSettings: CorSettings = CorSettings(),
    var workMode: OrderWorkMode = OrderWorkMode.PROD,
    var stubCase: OrderStubs = OrderStubs.NONE,
    var wsSession: WsSession = WsSession.NONE,

    var principal: PrincipalModel = PrincipalModel.NONE,
    val permissionsChain: MutableSet<UserPermissions> = mutableSetOf(),
    var permitted: Boolean = false,

    var requestId: OrderRequestId = OrderRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var orderRequest: Order = Order(),
    var orderFilterRequest: OrderFilter = OrderFilter(),

    var orderValidating: Order = Order(),
    var orderFilterValidating: OrderFilter = OrderFilter(),

    var orderValidated: Order = Order(),
    var orderFilterValidated: OrderFilter = OrderFilter(),

    var orderRepo: IRepoOrder = IRepoOrder.NONE,
    var orderRepoRead: Order = Order(), // То, что прочитали из репозитория
    var orderRepoPrepare: Order = Order(), // То, что готовим для сохранения в БД
    var orderRepoDone: Order = Order(),  // Результат, полученный из БД
    var ordersRepoDone: MutableList<Order> = mutableListOf(),

    var orderResponse: Order = Order(),
    var ordersResponse: MutableList<Order> = mutableListOf()
)