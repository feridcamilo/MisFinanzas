package com.android.data.remote.model.result

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SendMastersResult(
    @SerializedName("recibirMaestrosResult")
    val results: Boolean
) : Serializable
