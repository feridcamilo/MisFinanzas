package com.android.data.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ServerDateTime(
    @SerializedName("getServerDateTimeResult")
    val result: String
) : Serializable
