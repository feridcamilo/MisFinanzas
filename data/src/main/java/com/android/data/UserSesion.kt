package com.android.data

import com.android.data.local.model.UserVO
import com.android.data.remote.model.User
import com.android.data.utils.DateUtils

class UserSesion {
    companion object {
        private var INSTANCE: UserVO? = null
        private var FIRST_OPEN: Boolean = true

        fun getUser(): UserVO? {
            return INSTANCE
        }

        fun hasUser(): Boolean {
            return INSTANCE != null
        }

        fun setUser(user: UserVO) {
            INSTANCE = user
        }

        fun setUser(user: User) {
            val userVO = UserVO(user.Usuario, user.IdCliente, user.Nombres, user.Apellidos, user.Correo, DateUtils.getCurrentDateTime())
            INSTANCE = userVO
        }

        fun isFirstOpen(): Boolean {
            return FIRST_OPEN
        }

        fun setFirstOpen(value: Boolean) {
            FIRST_OPEN = value
        }
    }
}
