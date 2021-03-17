package com.android.misfinanzas.mappers

import com.android.domain.model.*
import com.android.misfinanzas.models.MasterModel
import com.android.misfinanzas.utils.toLatLng
import com.android.misfinanzas.utils.toLatLngString

class MastersMapper {

    fun mapToDomain(model: MasterModel): Master {
        return Master(
            model.id,
            model.name,
            model.latLng?.toLatLngString(),
            model.enabled
        )
    }

    fun map(domain: Master): MasterModel {
        return MasterModel(
            domain.id,
            domain.name,
            domain.latLng?.toLatLng(),
            domain.enabled
        )
    }

    fun map(domain: Person): MasterModel {
        return MasterModel(
            domain.id,
            domain.name,
            null,
            domain.enabled
        )
    }

    fun map(domain: Place): MasterModel {
        return MasterModel(
            domain.id,
            domain.name,
            domain.latLng?.toLatLng(),
            domain.enabled
        )
    }

    fun map(domain: Category): MasterModel {
        return MasterModel(
            domain.id,
            domain.name,
            null,
            domain.enabled
        )
    }

    fun map(domain: Debt): MasterModel {
        return MasterModel(
            domain.id,
            domain.name,
            null,
            domain.enabled
        )
    }

}
