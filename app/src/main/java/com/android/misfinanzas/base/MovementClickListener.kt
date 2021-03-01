package com.android.misfinanzas.base

import com.android.misfinanzas.models.MovementModel

interface MovementClickListener {
    fun onMovementClicked(movement: MovementModel?)
    fun onDiscardMovementClicked(id: Int)
}
