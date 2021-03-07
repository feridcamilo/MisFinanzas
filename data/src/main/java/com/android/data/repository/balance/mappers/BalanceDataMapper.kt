package com.android.data.repository.balance.mappers

import com.android.data.local.model.BalanceVO
import com.android.data.remote.model.BalanceDTO
import com.android.domain.model.Balance

class BalanceDataMapper {

    fun mapToVO(dto: BalanceDTO): BalanceVO {
        return BalanceVO(
            dto.IngresosEfectivo.toDouble(),
            dto.EgresosEfectivo.toDouble(),
            dto.IngresosElectronico.toDouble(),
            dto.EgresosElectronico.toDouble(),
            dto.Retiros.toDouble(),
            dto.ComprasTC.toDouble(),
            dto.TengoEfectivo.toDouble(),
            dto.TengoElectronico.toDouble(),
            dto.TengoTotal.toDouble(),
            dto.DiferenciaIngresos.toDouble(),
            dto.DiferenciaEgresos.toDouble(),
            dto.TotalIngresos.toDouble(),
            dto.TotalEgresos.toDouble()
        )
    }

    fun map(vo: BalanceVO): Balance {
        return Balance(
            vo.IngresosEfectivo,
            vo.EgresosEfectivo,
            vo.IngresosElectronico,
            vo.EgresosElectronico,
            vo.Retiros,
            vo.ComprasTC,
            vo.TengoEfectivo,
            vo.TengoElectronico,
            vo.TengoTotal,
            vo.DiferenciaIngresos,
            vo.DiferenciaEgresos,
            vo.TotalIngresos,
            vo.TotalEgresos
        )
    }

    fun map(dto: BalanceDTO): Balance {
        val vo = mapToVO(dto)
        return map(vo)
    }

}
