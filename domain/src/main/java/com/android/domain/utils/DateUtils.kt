package com.android.domain.utils

import com.android.domain.UserSesion
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
            return UserSesion.getServerTimeZone()!!.getOffset(date.time)
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

        fun getTimeZone(): TimeZone {
            val serverDateTime = UserSesion.getServerDateTime()
            val currentDateTime = getCurrentDateTime()
            val gmt = getGTMDifference(currentDateTime, serverDateTime) //current server difference
            return TimeZone.getTimeZone(gmt)
        }

        fun getGTMDifference(date1: Date, date2: Date): String {
            val MILLI_TO_HOUR = 1000 * 60 * 60
            val MILI_TO_MIN = 60 * 1000

            val diff = if (date1.time > date2.time) (date1.time - date2.time).toInt() else (date2.time - date1.time).toInt()
            var diffHours = diff / MILLI_TO_HOUR
            var diffMinutes = diff / MILI_TO_MIN % 60

            //fix to add 1 min (to substract) to always be before server time
            if (diffMinutes == 59) {
                diffMinutes = 0
                diffHours += 1
            } else {
                diffMinutes += 1
            }

            val hours = if (diffHours < 10) "0$diffHours" else diffHours
            val minutes = if (diffMinutes < 10) "0$diffMinutes" else diffMinutes
            val sign = if (date1.time > date2.time) "-" else "+"
            val gmt = "GMT$sign$hours:$minutes"

            return gmt
        }
    }
}
