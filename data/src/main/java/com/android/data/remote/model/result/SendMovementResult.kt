package com.android.data.remote.model.result

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SendMovementResult(
    @SerializedName("recibirMovimientosResult")
    val results: Boolean
) : Serializable
