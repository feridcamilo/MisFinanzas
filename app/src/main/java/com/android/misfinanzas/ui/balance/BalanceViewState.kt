package com.android.misfinanzas.ui.balance

sealed class BalanceViewState {
    object PageLoading : BalanceViewState()
    object UserLoaded : BalanceViewState()
    object ErrorLoadingUser : BalanceViewState()
    object BalanceLoaded : BalanceViewState()
    object ErrorLoadingBalance : BalanceViewState()
    object MovementDiscarded : BalanceViewState()
    object ErrorDiscardingMovement : BalanceViewState()
}
