package com.android.data.remote.model.result

import com.android.data.remote.model.UserDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResult(
    @SerialName("getUsuarioResult")
    val result: UserDTO
)
