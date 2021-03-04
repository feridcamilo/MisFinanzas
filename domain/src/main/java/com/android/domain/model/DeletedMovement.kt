package com.android.domain.model

import java.io.Serializable

data class DeletedMovement(
    val id: Int,
    val idMovement: Int
) : Serializable
