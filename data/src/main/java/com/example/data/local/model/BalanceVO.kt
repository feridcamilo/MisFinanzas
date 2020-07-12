package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.math.BigDecimal

@Entity(tableName = "Balance")
data class BalanceVO(
    @PrimaryKey
    val id: Int,
    val IngresosEfectivo: BigDecimal,
    val EgresosEfectivo: BigDecimal,
    val IngresosElectronico: BigDecimal,
    val EgresosElectronico: BigDecimal,
    val Retiros: BigDecimal,
    val CompraTC: BigDecimal,
    val TengoEfectivo: BigDecimal,
    val TengoElectronico: BigDecimal,
    val TengoTotal: BigDecimal,
    val DiferenciaIngresos: BigDecimal,
    val DiferenciaEgresos: BigDecimal,
    val TotalIngresos: BigDecimal,
    val TotalEgresos: BigDecimal
) : Serializable
