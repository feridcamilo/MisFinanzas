package com.android.misfinanzas.mappers

import com.android.domain.model.Movement
import com.android.domain.model.MovementDetailed
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
            null,
            domain.placeId,
            null,
            domain.categoryId,
            null,
            domain.date,
            domain.debtId,
            null,
            domain.dateEntry,
            domain.dateLastUpd
        )
    }

    fun map(domain: MovementDetailed): MovementModel {
        return MovementModel(
            domain.idMovement,
            domain.idType,
            domain.value,
            domain.description,
            domain.personId,
            domain.personName,
            domain.placeId,
            domain.placeName,
            domain.categoryId,
            domain.categoryName,
            domain.date,
            domain.debtId,
            domain.debtName,
            domain.dateEntry,
            domain.dateLastUpd
        )
    }

}
