package com.android.data.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DeletedMovementDTO(
    @SerializedName("eliminarMovimientosResult")
    val result: Boolean,
    @SerializedName("getMovimientosEliminadosResult")
    val results: List<Int>
) : Serializable

data class DeletedMovement(
    val ids: List<Int>
)
