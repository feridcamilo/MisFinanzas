package com.android.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Balance")
data class BalanceVO(
    @PrimaryKey
    val id: Int = 0,
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
