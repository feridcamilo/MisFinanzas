package com.example.data.remote.model

import com.example.data.local.model.PlaceVO
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlaceDTO(
    @SerializedName("getLugaresResult")
    val results: List<PlaceVO>
) : Serializable
