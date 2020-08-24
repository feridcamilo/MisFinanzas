package com.android.data.utils

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

        fun getServerTimeOffset(date: Date): Int {
            return getTimeZone().getOffset(date.time)
        }

        private fun getTimeZone(): TimeZone {
            return TimeZone.getTimeZone("GMT-02:00") //current server config
        }

        fun getDateTimeFormatToWebService(): SimpleDateFormat {
            return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        }

        fun getDateTimeFormat(): SimpleDateFormat {
            return SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
        }

        fun getDateFormat(): SimpleDateFormat {
            return SimpleDateFormat("dd/MM/yyyy", Locale.US)
        }
    }
}
