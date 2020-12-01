package com.android.data.remote.model.result

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DeletedMovementResult(
    @SerializedName("eliminarMovimientosResult")
    val result: Boolean,
    @SerializedName("getMovimientosEliminadosResult")
    val results: List<Int>
) : Serializable
