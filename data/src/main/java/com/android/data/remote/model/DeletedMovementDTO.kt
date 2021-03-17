package com.android.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class DeletedMovementDTO(
    val ids: List<Int>
)
