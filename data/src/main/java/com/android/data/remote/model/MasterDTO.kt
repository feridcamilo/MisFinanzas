package com.android.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class MasterDTO(
    val Id: Int,
    val Nombre: String,
    val LatLng: String? = null,
    val Activo: Boolean,
    val FechaRegistro: String? = null,
    val FechaActualizacion: String? = null
)

