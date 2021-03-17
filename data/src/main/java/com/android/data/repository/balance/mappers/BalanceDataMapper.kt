package com.android.data.repository.balance.mappers

import com.android.data.local.model.BalanceVO
import com.android.domain.model.Balance

class BalanceDataMapper {

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

}
