package com.android.data.local.model

data class BalanceVO(
    val IngresosEfectivo: Double,
    val EgresosEfectivo: Double,
    val IngresosElectronico: Double,
    val EgresosElectronico: Double,
    val Retiros: Double,
    val ComprasTC: Double,
    val TengoEfectivo: Double,
    val TengoElectronico: Double,
    val TengoTotal: Double,
    val DiferenciaIngresos: Double,
    val DiferenciaEgresos: Double,
    val TotalIngresos: Double,
    val TotalEgresos: Double
)
