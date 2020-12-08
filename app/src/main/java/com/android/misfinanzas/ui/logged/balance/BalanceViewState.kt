package com.android.misfinanzas.ui.logged.balance

import com.android.domain.model.Balance

sealed class BalanceViewState {
    class BalanceLoaded(val balance: Balance) : BalanceViewState()
    object ErrorLoadingBalance : BalanceViewState()
    class DiscardedMovsLoaded(val discardedIds: List<Int>) : BalanceViewState()
    object MovementDiscarded : BalanceViewState()
    object ErrorDiscardingMovement : BalanceViewState()
}
