package com.android.data.remote.model.result

import com.android.data.remote.model.MovementDTO
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MovementResult(
    @SerializedName("getMovimientosResult")
    val results: List<MovementDTO>
) : Serializable
