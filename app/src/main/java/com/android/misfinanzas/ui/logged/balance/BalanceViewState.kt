package com.android.misfinanzas.ui.logged.balance

import com.android.misfinanzas.models.BalanceModel

sealed class BalanceViewState {

    class BalanceLoaded(val balance: BalanceModel) : BalanceViewState()
    object ErrorLoadingBalance : BalanceViewState()
    class DiscardedMovsLoaded(val discardedIds: List<Int>) : BalanceViewState()
    object MovementDiscarded : BalanceViewState()
    object ErrorDiscardingMovement : BalanceViewState()

}
