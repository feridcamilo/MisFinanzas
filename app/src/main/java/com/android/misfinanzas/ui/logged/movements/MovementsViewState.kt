package com.android.misfinanzas.ui.logged.movements

import com.android.misfinanzas.models.MovementModel

sealed class MovementsViewState {

    class MovementsLoaded(val movements: List<MovementModel>) : MovementsViewState()

}
