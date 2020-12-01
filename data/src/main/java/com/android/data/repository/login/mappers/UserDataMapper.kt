package com.android.data.repository.login.mappers

import com.android.data.local.model.UserVO
import com.android.data.remote.model.UserDTO
import model.User

class UserDataMapper {

    fun mapToVO(dto: UserDTO): UserVO {
        return UserVO(
            dto.result.Usuario,
            dto.result.IdCliente,
            dto.result.Nombres,
            dto.result.Apellidos,
            dto.result.Correo,
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
}
