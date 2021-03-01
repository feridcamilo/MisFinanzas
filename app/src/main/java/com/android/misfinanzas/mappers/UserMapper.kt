package com.android.misfinanzas.mappers

import com.android.domain.model.User
import com.android.misfinanzas.models.UserModel

class UserMapper {

    fun map(domain: User): UserModel {
        return UserModel(
            domain.user,
            domain.clientId,
            domain.name,
            domain.lastName,
            domain.email,
            domain.lastSyncMovements,
            domain.lastSyncMasters
        )
    }

    fun mapToDomain(model: UserModel): User {
        return User(
            model.user,
            model.clientId,
            model.name,
            model.lastName,
            model.email,
            model.lastSyncMovements,
            model.lastSyncMasters
        )
    }

}
