package com.android.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SendMovementDTO(
    val idCliente: Int,
    val movimientos: List<MovementDTO>
)
