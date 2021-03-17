package com.android.data.remote.model.result

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeletedMovementResult(
    @SerialName("getMovimientosEliminadosResult")
    val results: List<Int>
)
