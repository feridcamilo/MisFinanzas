package com.android.data.repository.user.mappers

import com.android.data.local.model.UserVO
import com.android.data.remote.model.UserDTO
import com.android.domain.model.User

class UserDataMapper {

    fun mapToVO(dto: UserDTO): UserVO {
        return UserVO(
            dto.Usuario,
            dto.IdCliente,
            dto.Nombres,
            dto.Apellidos,
            dto.Correo,
            null,
            null
        )
    }

    fun mapToVO(user: User): UserVO {
        return UserVO(
            user.user,
            user.clientId,
            user.name,
            user.lastName,
            user.email,
            user.lastSyncMovements,
            user.lastSyncMasters
        )
    }

    fun map(vo: UserVO): User {
        return User(
            vo.user,
            vo.clientId,
            vo.name,
            vo.lastName,
            vo.email,
            vo.lastSyncMovements,
            vo.lastSyncMasters
        )
    }

    fun map(dto: UserDTO): User {
        val vo = mapToVO(dto)
        return map(vo)
    }

}
