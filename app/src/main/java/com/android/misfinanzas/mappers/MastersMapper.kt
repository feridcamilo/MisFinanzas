package com.android.misfinanzas.mappers

import com.android.domain.model.*
import com.android.misfinanzas.models.MasterModel

class MastersMapper {

    fun mapToDomain(model: MasterModel): Master {
        return Master(
            model.id,
            model.name,
            model.enabled
        )
    }

    fun map(domain: Master): MasterModel {
        return MasterModel(
            domain.id,
            domain.name,
            domain.enabled
        )
    }

    fun map(domain: Person): MasterModel {
        return MasterModel(
            domain.id,
            domain.name,
            domain.enabled
        )
    }

    fun map(domain: Place): MasterModel {
        return MasterModel(
            domain.id,
            domain.name,
            domain.enabled
        )
    }

    fun map(domain: Category): MasterModel {
        return MasterModel(
            domain.id,
            domain.name,
            domain.enabled
        )
    }

    fun map(domain: Debt): MasterModel {
        return MasterModel(
            domain.id,
            domain.name,
            domain.enabled
        )
    }

}
