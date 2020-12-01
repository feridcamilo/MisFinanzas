package com.android.data.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserDTO(
    @SerializedName("getUsuarioResult")
    val result: UserResult
) : Serializable

data class UserResult(
    val Usuario: String,
    val IdCliente: Int,
    val Nombres: String,
    val Apellidos: String,
    val Correo: String
) : Serializable
