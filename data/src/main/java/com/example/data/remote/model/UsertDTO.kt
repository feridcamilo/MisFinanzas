package com.example.data.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserDTO(
    @SerializedName("getUsuarioResult")
    val result: User
) : Serializable

data class User(
    val Usuario: String,
    val IdCliente: Int,
    val Nombres: String,
    val Apellidos: String,
    val Correo: String
) : Serializable
