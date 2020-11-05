package com.android.data

import com.android.data.local.model.UserVO
import com.android.data.remote.model.User
import com.android.data.utils.DateUtils
import java.util.*

class UserSesion {
    companion object {
        private var INSTANCE: UserVO? = null
        private var FIRST_OPEN: Boolean = true
        private lateinit var SERVER_DATETIME: Date
        private lateinit var SERVER_TIME_ZONE: TimeZone

        fun getUser(): UserVO? {
            return INSTANCE
        }

        fun hasUser(): Boolean {
            return INSTANCE != null
        }

        fun setUser(user: UserVO) {
            INSTANCE = user
        }

        fun updateLastSyncMovements(date: Date) {
            val currentUser = getUser()
            if (currentUser != null) {
                val newUser = UserVO(
                    currentUser.user,
                    currentUser.clientId,
                    currentUser.name,
                    currentUser.lastName,
                    currentUser.email,
                    date,
                    currentUser.lastSyncMasters
                )
                setUser(newUser)
            }
        }

        fun updateLastSyncMasters(date: Date) {
            val currentUser = getUser()
            if (currentUser != null) {
                val newUser = UserVO(
                    currentUser.user,
                    currentUser.clientId,
                    currentUser.name,
                    currentUser.lastName,
                    currentUser.email,
                    currentUser.lastSyncMovements,
                    date
                )
                setUser(newUser)
            }
        }

        fun setUser(user: User) {
            val userVO = UserVO(
                user.Usuario,
                user.IdCliente,
                user.Nombres,
                user.Apellidos,
                user.Correo,
                DateUtils.getCurrentDateTime(),
                DateUtils.getCurrentDateTime()
            )
            INSTANCE = userVO
        }

        fun isFirstOpen(): Boolean {
            return FIRST_OPEN
        }

        fun setFirstOpen(value: Boolean) {
            FIRST_OPEN = value
        }

        fun setServerDateTime(date: Date) {
            SERVER_DATETIME = date
            setServerTimeZone()
        }

        fun getServerDateTime(): Date {
            return SERVER_DATETIME
        }

        private fun setServerTimeZone() {
            SERVER_TIME_ZONE = DateUtils.getTimeZone()
        }

        fun getServerTimeZone(): TimeZone {
            return SERVER_TIME_ZONE
        }
    }
}
