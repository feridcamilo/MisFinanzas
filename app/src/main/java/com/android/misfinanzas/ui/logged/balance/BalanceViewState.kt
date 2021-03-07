package com.android.misfinanzas.ui.logged.balance

import com.android.misfinanzas.models.BalanceModel
import com.android.misfinanzas.models.MovementModel

sealed class BalanceViewState {

    class BalanceLoaded(val balance: BalanceModel) : BalanceViewState()
    class PotentialsMovementsLoaded(val potentialsMovements: List<MovementModel>) : BalanceViewState()
    object MovementDiscarded : BalanceViewState()

}
