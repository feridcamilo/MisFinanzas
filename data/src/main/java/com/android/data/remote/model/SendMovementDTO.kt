package com.android.data.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SendMovementDTO(
    @SerializedName("recibirMovimientosResult")
    val results: Boolean
) : Serializable

data class SendMovement(
    val idCliente: Int,
    val movimientos: List<Movement>
)
