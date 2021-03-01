package com.android.data.repository.master.mappers

import com.android.data.local.model.DebtVO
import com.android.data.remote.model.MasterDTO
import com.android.domain.model.Debt
import com.android.domain.model.Master

class DebtDataMapper {

    fun mapToVO(dto: MasterDTO): DebtVO {
        return DebtVO(
            dto.Id,
            dto.Nombre,
            dto.Activo
        )
    }

    fun mapToVO(debt: Debt): DebtVO {
        return DebtVO(
            debt.id,
            debt.name,
            debt.enabled
        )
    }

    fun map(master: Master): Debt {
        return Debt(
            master.id,
            master.name,
            master.enabled
        )
    }

    fun map(vo: DebtVO): Debt {
        return Debt(
            vo.id,
            vo.name,
            vo.enabled
        )
    }

    fun map(dto: MasterDTO): Debt {
        val vo = mapToVO(dto)
        return map(vo)
    }

}
