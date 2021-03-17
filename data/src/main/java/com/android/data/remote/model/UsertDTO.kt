package com.android.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val Usuario: String,
    val IdCliente: Int,
    val Nombres: String,
    val Apellidos: String,
    val Correo: String
)
