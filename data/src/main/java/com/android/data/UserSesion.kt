package com.android.data

import com.android.data.local.model.UserVO
import com.android.data.remote.model.User
import com.android.data.utils.DateUtils
import com.android.data.utils.StringUtils.Companion.EMPTY
import java.util.*

class UserSesion {
    companion object {
        private var INSTANCE: UserVO? = null
        private var FIRST_OPEN: Boolean = true
        private var SERVER_DATETIME: Date? = null
        private var SERVER_TIME_ZONE: TimeZone? = null

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
            return SERVER_DATETIME!!
        }

        fun setServerTimeZone(timeZone: String = EMPTY) {
            SERVER_TIME_ZONE = if (SERVER_DATETIME != null && timeZone.isEmpty()) {
                DateUtils.getTimeZone()
            } else {
                TimeZone.getTimeZone(timeZone)
            }
        }

        fun getServerTimeZone(): TimeZone? {
            return SERVER_TIME_ZONE
        }
    }
}
