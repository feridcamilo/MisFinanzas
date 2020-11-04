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

        private fun getServerDateTime(): Date {
            return SERVER_DATETIME
        }

        private fun setServerTimeZone() {
            SERVER_TIME_ZONE = getTimeZone()
        }

        fun getServerTimeZone(): TimeZone {
            return SERVER_TIME_ZONE
        }

        private fun getTimeZone(): TimeZone {
            val serverDateTime = getServerDateTime()
            val currentDateTime = DateUtils.getCurrentDateTime()
            val gmt = getGTMDifference(currentDateTime, serverDateTime) //current server difference
            return TimeZone.getTimeZone(gmt)
        }

        private fun getGTMDifference(date1: Date, date2: Date): String {
            val MILLI_TO_HOUR = 1000 * 60 * 60

            val diff = if (date1.time > date2.time) (date1.time - date2.time).toInt() else (date2.time - date1.time).toInt()
            val diffHours = diff / if (MILLI_TO_HOUR == 0) 1 else MILLI_TO_HOUR
            val diffMinutes = diff / (60 * 1000) % 60

            val hours = if (diffHours < 10) "0$diffHours" else diffHours
            val minutes = if (diffMinutes < 10) "0$diffMinutes" else diffMinutes
            val sign = if (date1.time > date2.time) "-" else "+"
            val gmt = "GMT$sign$hours:$minutes"

            return gmt
        }
    }
}
