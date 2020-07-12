package com.android.data.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigDecimal

data class BalanceDTO(
    @SerializedName("getSaldosResult")
    val result: Balance
) : Serializable

data class Balance(
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
