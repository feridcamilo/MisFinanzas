package com.android.data

import com.android.data.local.model.UserVO
import com.android.data.remote.model.User
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class UserSesion {
    companion object {
        private var INSTANCE: UserVO? = null

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
            val userVO = UserVO(user.Usuario, user.IdCliente, user.Nombres, user.Apellidos, user.Correo, getCurrentDateTime())
            INSTANCE = userVO
        }

        fun getTimeZone(): TimeZone {
            return TimeZone.getTimeZone("GMT-02:00") //current server config
        }

        fun getCurrentDateTime(): Date {
            val longDate = Calendar.getInstance().timeInMillis
            return Date(longDate + getTimeZone().getOffset(longDate))
        }

        fun getDateTimeFormatWeb(): SimpleDateFormat {
            return SimpleDateFormat("yyyy-MM-d HH:mm:ss", Locale.US)
        }

        fun getDateTimeFormat(): SimpleDateFormat {
            return SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
        }

        fun getDateFormat(): SimpleDateFormat {
            return SimpleDateFormat("dd/MM/yyyy", Locale.US)
        }

        fun getMoneyFormat(): NumberFormat {
            return DecimalFormat("$ ###,###,##0.00")
        }
    }
}