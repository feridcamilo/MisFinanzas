package com.android.data.repository.master.mappers

import com.android.data.local.model.PersonVO
import com.android.data.remote.model.MasterDTO
import com.android.domain.model.Master
import com.android.domain.model.Person

class PersonDataMapper {

    fun mapToVO(dto: MasterDTO): PersonVO {
        return PersonVO(
            dto.Id,
            dto.Nombre,
            dto.Activo
        )
    }

    fun mapToVO(person: Person): PersonVO {
        return PersonVO(
            person.id,
            person.name,
            person.enabled
        )
    }

    fun map(master: Master): Person {
        return Person(
            master.id,
            master.name,
            master.enabled
        )
    }

    fun map(vo: PersonVO): Person {
        return Person(
            vo.id,
            vo.name,
            vo.enabled
        )
    }

    fun map(dto: MasterDTO): Person {
        val vo = mapToVO(dto)
        return map(vo)
    }

}
