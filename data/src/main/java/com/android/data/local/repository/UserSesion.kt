package com.android.data.local.repository

import com.android.data.local.model.UserVO
import com.android.data.remote.model.User

class UserSesion {
    companion object {
        private var INSTANCE: UserVO? = null

        fun getUser(): UserVO? {
            return INSTANCE
        }

        fun setUser(user: UserVO) {
            this.INSTANCE = user
        }

        fun setUser(user: User) {
            val userVO = UserVO(user.Usuario, user.IdCliente, user.Nombres, user.Apellidos, user.Correo)
            this.INSTANCE = userVO
        }
    }
}