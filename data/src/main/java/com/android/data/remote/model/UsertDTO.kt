package com.android.data.remote.model

import java.io.Serializable

data class UserDTO(
    val Usuario: String,
    val IdCliente: Int,
    val Nombres: String,
    val Apellidos: String,
    val Correo: String
) : Serializable
