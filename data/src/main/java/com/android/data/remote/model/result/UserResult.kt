package com.android.data.remote.model.result

import com.android.data.remote.model.UserDTO
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserResult(
    @SerializedName("getUsuarioResult")
    val result: UserDTO
) : Serializable
