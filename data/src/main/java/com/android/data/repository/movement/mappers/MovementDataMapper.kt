package com.android.data.repository.movement.mappers

import com.android.data.local.model.MovementDetailedVO
import com.android.data.local.model.MovementVO
import com.android.data.remote.model.MovementDTO
import com.android.domain.model.Movement
import com.android.domain.model.MovementDetailed
import com.android.domain.utils.DateUtils
import com.android.domain.utils.StringUtils.Companion.EMPTY
import java.util.*

class MovementDataMapper {

    fun mapToVO(dto: MovementDTO): MovementVO {
        return MovementVO(
            dto.id,
            dto.idMovType,
            dto.value,
            dto.description,
            dto.idPerson,
            dto.idPlace,
            dto.idCategory,
            DateUtils.deserialize(dto.date),
            dto.idDebt,
            DateUtils.deserialize(dto.dateEntry),
            DateUtils.deserialize(dto.dateUpdate)
        )
    }

    fun mapToVO(movement: Movement): MovementVO {
        return MovementVO(
            movement.idMovement,
            movement.idType,
            movement.value,
            movement.description,
            movement.personId,
            movement.placeId,
            movement.categoryId,
            movement.date,
            movement.debtId,
            movement.dateEntry,
            movement.dateLastUpd
        )
    }

    fun mapToDTO(vo: MovementVO, lastSync: Date): MovementDTO {
        return MovementDTO(
            if (vo.dateEntry!! > lastSync) 0 else vo.idMovement, //if 0 is a new movement, else is a old mov updated,
            vo.value,
            vo.description,
            DateUtils.serialize(vo.date!!),
            DateUtils.serialize(vo.dateEntry),
            vo.dateLastUpd?.let { DateUtils.serialize(it) },
            vo.idType,
            vo.categoryId,
            vo.debtId,
            vo.personId,
            vo.placeId
        )
    }

    fun map(vo: MovementVO): Movement {
        return Movement(
            vo.idMovement,
            vo.idType,
            vo.value,
            vo.description,
            vo.personId,
            vo.placeId,
            vo.categoryId,
            vo.date,
            vo.debtId,
            vo.dateEntry,
            vo.dateLastUpd
        )
    }

    fun map(vo: MovementDetailedVO): MovementDetailed {
        return MovementDetailed(
            vo.idMovement,
            vo.idType,
            vo.value,
            vo.description,
            vo.personId,
            vo.personName,
            vo.placeId,
            vo.placeName,
            vo.categoryId,
            vo.categoryName,
            vo.date,
            vo.debtId,
            vo.debtName,
            vo.dateEntry,
            vo.dateLastUpd
        )
    }

    fun map(dto: MovementDTO): Movement {
        val balanceVO = mapToVO(dto)
        return map(balanceVO)
    }

}
