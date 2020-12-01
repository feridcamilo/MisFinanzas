package com.android.data.remote.model.result

import com.android.data.remote.model.MasterDTO
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlaceResult(
    @SerializedName("getLugaresResult")
    val results: List<MasterDTO>
) : Serializable
