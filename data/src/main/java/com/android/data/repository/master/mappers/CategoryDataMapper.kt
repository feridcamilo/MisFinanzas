package com.android.data.repository.master.mappers

import com.android.data.local.model.CategoryVO
import com.android.data.local.model.MasterVO
import com.android.data.remote.model.MasterDTO
import com.android.domain.model.Category
import com.android.domain.model.Master

class CategoryDataMapper {

    fun mapToVO(dto: MasterDTO): CategoryVO {
        return CategoryVO(
            dto.Id,
            dto.Nombre,
            dto.Activo
        )
    }

    fun mapToVO(category: Category): CategoryVO {
        return CategoryVO(
            category.id,
            category.name,
            category.enabled
        )
    }

    fun map(vo: MasterVO): Category {
        return Category(
            vo.id,
            vo.name,
            vo.enabled
        )
    }

    fun map(master: Master): Category {
        return Category(
            master.id,
            master.name,
            master.enabled
        )
    }

    fun map(vo: CategoryVO): Category {
        return Category(
            vo.id,
            vo.name,
            vo.enabled
        )
    }


    fun map(dto: MasterDTO): Category {
        val vo = mapToVO(dto)
        return map(vo)
    }

}
