package com.android.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "Balance")
data class BalanceVO(
    @PrimaryKey
    val id: Int = 0,
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
)
