package com.android.data.utils

import com.android.data.UserSesion
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        fun getCurrentDateTime(): Date {
            val longDate = Calendar.getInstance().timeInMillis
            return Date(longDate)
        }

        fun getDateTimeToWebService(date: Date): Date {
            return Date(date.time + getServerTimeOffset(date))
        }

        fun getDateToWebService(date: Date): Date {
            return Date(date.time - getServerTimeOffset(date))
        }

        private fun getServerTimeOffset(date: Date): Int {
            return UserSesion.getServerTimeZone().getOffset(date.time)
        }

        fun getDateTimeFormatToWebService(): SimpleDateFormat {
            return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        }

        fun getDateTimeFormat(): SimpleDateFormat {
            return SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
        }

        fun getDateTimeFormat_AM_PM(): SimpleDateFormat {
            return SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US)
        }

        fun getDateFormat(): SimpleDateFormat {
            return SimpleDateFormat("dd/MM/yyyy", Locale.US)
        }

        fun getCalendarFromStringDate(strDate: String): Calendar {
            val splitedDate = strDate.split(StringUtils.SLASH)
            val day = splitedDate[0]
            val month = splitedDate[1]
            val year = splitedDate[2]
            val calendar = Calendar.getInstance()
            calendar[Calendar.YEAR] = Integer.parseInt(year)
            calendar[Calendar.MONTH] = Integer.parseInt(month) - 1
            calendar[Calendar.DAY_OF_MONTH] = Integer.parseInt(day)
            return calendar
        }
    }
}
