package com.android.data.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlaceDTO(
    @SerializedName("getLugaresResult")
    val results: List<Master>
) : Serializable
