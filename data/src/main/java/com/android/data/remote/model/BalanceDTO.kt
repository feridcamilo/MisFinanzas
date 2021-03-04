package com.android.data.remote.model

import java.io.Serializable
import java.math.BigDecimal

data class BalanceDTO(
    val id: Int,
    val IngresosEfectivo: BigDecimal,
    val EgresosEfectivo: BigDecimal,
    val IngresosElectronico: BigDecimal,
    val EgresosElectronico: BigDecimal,
    val Retiros: BigDecimal,
    val ComprasTC: BigDecimal,
    val TengoEfectivo: BigDecimal,
    val TengoElectronico: BigDecimal,
    val TengoTotal: BigDecimal,
    val DiferenciaIngresos: BigDecimal,
    val DiferenciaEgresos: BigDecimal,
    val TotalIngresos: BigDecimal,
    val TotalEgresos: BigDecimal
) : Serializable
