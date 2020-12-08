package com.android.domain

import com.android.domain.model.User
import com.android.domain.utils.DateUtils
import com.android.domain.utils.StringUtils.Companion.EMPTY
import java.util.*

class UserSesion {
    companion object {
        private lateinit var INSTANCE: User
        private var FIRST_OPEN: Boolean = true
        private var SERVER_DATETIME: Date? = null
        private var SERVER_TIME_ZONE: TimeZone? = null

        fun getUser(): User {
            return INSTANCE
        }

        fun getClientId(): String {
            return INSTANCE.clientId.toString()
        }

        fun setUser(user: User) {
            INSTANCE = user
        }

        fun updateLastSyncMovements(date: Date) {
            val currentUser = getUser()
            if (currentUser != null) {
                val newUser = User(
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
                val newUser = User(
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
