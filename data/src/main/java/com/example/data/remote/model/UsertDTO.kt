package com.example.data.remote.model

import com.example.data.local.model.UserVO
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserDTO(
    @SerializedName("getUsuarioResult")
    val result: UserVO
) : Serializable
