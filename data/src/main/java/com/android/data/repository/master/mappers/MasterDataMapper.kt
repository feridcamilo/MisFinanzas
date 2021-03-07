package com.android.data.repository.master.mappers

import com.android.data.remote.model.MasterDTO
import com.android.domain.model.Master

class MasterDataMapper {

    fun mapToDTO(domain: Master): MasterDTO {
        return MasterDTO(
            domain.id,
            domain.name,
            domain.enabled
        )
    }

}
