package com.example.data.remote.model

import com.example.data.local.model.MovementVO
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MovementDTO(
    @SerializedName("getMovimientosResult")
    val results: List<MovementVO>
) : Serializable
