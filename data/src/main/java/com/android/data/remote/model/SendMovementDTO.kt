package com.android.data.remote.model

data class SendMovementDTO(
    val idCliente: Int,
    val movimientos: List<MovementDTO>
)
