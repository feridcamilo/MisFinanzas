package com.android.data.remote.model

import java.io.Serializable
import java.math.BigDecimal
import java.util.*

data class MovementDTO(
    val IdMovimiento: Int,
    val Valor: BigDecimal,
    val Descripcion: String,
    val FechaMovimiento: Date,
    val FechaIngreso: Date?,
    val FechaActualizacion: Date?,
    val IdTipoMovimiento: Int,
    val DescTipoMovimiento: String,
    val IdCategoria: Int?,
    val DescCategoria: String,
    val IdDeuda: Int?,
    val DescDeuda: String,
    val IdPersona: Int?,
    val NombrePersona: String,
    val IdLugar: Int?,
    val NombreLugar: String
) : Serializable
