package com.android.misfinanzas.mappers

import com.android.domain.model.Movement
import com.android.misfinanzas.models.MovementModel

class MovementMapper {

    fun mapToDomain(model: MovementModel): Movement {
        return Movement(
            model.idMovement,
            model.idType,
            model.value,
            model.description,
            model.personId,
            model.placeId,
            model.categoryId,
            model.date,
            model.debtId,
            model.dateEntry,
            model.dateLastUpd
        )
    }

    fun map(domain: Movement): MovementModel {
        return MovementModel(
            domain.idMovement,
            domain.idType,
            domain.value,
            domain.description,
            domain.personId,
            domain.placeId,
            domain.categoryId,
            domain.date,
            domain.debtId,
            domain.dateEntry,
            domain.dateLastUpd
        )
    }

}
