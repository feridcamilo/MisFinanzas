package com.android.misfinanzas.base

import com.android.domain.model.Movement

interface MovementClickListener {
    fun onMovementClicked(movement: Movement?)
    fun onDiscardMovementClicked(id: Int, position: Int)
}
