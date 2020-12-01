package com.android.data.repository.master.mappers

import com.android.data.local.model.MasterVO
import com.android.data.remote.model.MasterDTO
import com.android.domain.model.*

class MasterDataMapper {

    fun mapToVO(dto: MasterDTO): MasterVO {
        return MasterVO(
            dto.Id,
            dto.Nombre,
            dto.Activo
        )
    }

    fun mapToVO(master: Master): MasterVO {
        return MasterVO(
            master.id,
            master.name,
            master.enabled
        )
    }

    fun mapToDTO(master: Master): MasterDTO {
        return MasterDTO(
            master.id,
            master.name,
            master.enabled
        )
    }

    fun map(vo: MasterVO): Master {
        return Master(
            vo.id,
            vo.name,
            vo.enabled
        )
    }

    fun map(dto: MasterDTO): Master {
        val balanceVO = mapToVO(dto)
        return map(balanceVO)
    }

    fun map(category: Category): Master {
        return Master(
            category.id,
            category.name,
            category.enabled
        )
    }

    fun map(debt: Debt): Master {
        return Master(
            debt.id,
            debt.name,
            debt.enabled
        )
    }

    fun map(person: Person): Master {
        return Master(
            person.id,
            person.name,
            person.enabled
        )
    }

    fun map(place: Place): Master {
        return Master(
            place.id,
            place.name,
            place.enabled
        )
    }

}
