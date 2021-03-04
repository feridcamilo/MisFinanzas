package com.android.misfinanzas.mappers

import com.android.domain.model.Balance
import com.android.misfinanzas.models.BalanceModel

class BalanceMapper {

    fun map(domain: Balance): BalanceModel {
        return BalanceModel(
            domain.TengoEfectivo,
            domain.TengoElectronico,
            domain.TengoTotal
        )
    }

}
