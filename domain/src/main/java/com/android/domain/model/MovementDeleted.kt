package com.android.domain.model

import java.io.Serializable

data class MovementDeleted(
    val id: Int,
    val idMovement: Int
) : Serializable
