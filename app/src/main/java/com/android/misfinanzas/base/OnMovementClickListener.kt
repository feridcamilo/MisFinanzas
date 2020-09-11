package com.android.misfinanzas.base

import com.android.data.local.model.MovementVO

interface OnMovementClickListener {
    fun onMovementClicked(movement: MovementVO?)
    fun onDiscardMovementClicked(id: Int, position: Int)
}
