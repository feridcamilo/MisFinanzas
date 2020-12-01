package com.android.data.repository.master.mappers

import com.android.data.local.model.MasterVO
import com.android.data.local.model.PlaceVO
import com.android.data.remote.model.MasterDTO
import com.android.domain.model.Master
import com.android.domain.model.Place

class PlaceDataMapper {

    fun mapToVO(dto: MasterDTO): PlaceVO {
        return PlaceVO(
            dto.Id,
            dto.Nombre,
            dto.Activo
        )
    }

    fun mapToVO(place: Place): PlaceVO {
        return PlaceVO(
            place.id,
            place.name,
            place.enabled
        )
    }

    fun map(vo: MasterVO): Place {
        return Place(
            vo.id,
            vo.name,
            vo.enabled
        )
    }

    fun map(master: Master): Place {
        return Place(
            master.id,
            master.name,
            master.enabled
        )
    }

    fun map(vo: PlaceVO): Place {
        return Place(
            vo.id,
            vo.name,
            vo.enabled
        )
    }

    fun map(dto: MasterDTO): Place {
        val vo = mapToVO(dto)
        return map(vo)
    }

}
