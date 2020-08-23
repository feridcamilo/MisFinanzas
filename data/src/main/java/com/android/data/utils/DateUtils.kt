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
            val longDate = date.time
            val serverOffset = getTimeZone().getOffset(longDate)
            return Date(longDate + serverOffset)
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
