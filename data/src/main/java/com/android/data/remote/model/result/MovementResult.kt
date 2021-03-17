package com.android.data.remote.model.result

import com.android.data.remote.model.MovementDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovementResult(
    @SerialName("getMovimientosResult")
    val results: List<MovementDTO>
)
