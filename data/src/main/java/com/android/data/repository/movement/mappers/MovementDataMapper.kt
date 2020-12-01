package com.android.data.repository.movement.mappers

import com.android.data.local.model.MovementVO
import com.android.data.remote.model.MovementDTO
import com.android.domain.model.Movement
import com.android.domain.utils.StringUtils.Companion.EMPTY

class MovementDataMapper {

    fun mapToVO(dto: MovementDTO): MovementVO {
        return MovementVO(
            dto.IdMovimiento,
            dto.IdTipoMovimiento,
            dto.Valor,
            dto.Descripcion,
            dto.IdPersona,
            dto.IdLugar,
            dto.IdCategoria,
            dto.FechaMovimiento,
            dto.IdDeuda,
            dto.FechaIngreso,
            dto.FechaActualizacion
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

    fun mapToDTO(movement: Movement): MovementDTO {
        return MovementDTO(
            movement.idMovement,
            movement.value,
            movement.description,
            movement.date!!,
            movement.dateEntry,
            movement.dateLastUpd,
            movement.idType,
            EMPTY,
            movement.categoryId,
            EMPTY,
            movement.debtId,
            EMPTY,
            movement.personId,
            EMPTY,
            movement.placeId,
            EMPTY
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

    fun map(dto: MovementDTO): Movement {
        val balanceVO = mapToVO(dto)
        return map(balanceVO)
    }

}
