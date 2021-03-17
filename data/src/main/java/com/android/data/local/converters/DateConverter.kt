package com.android.data.local.converters

import androidx.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun toDate(time: Long?): Date? {
        var date: Date? = null
        if (time != null) {
            date = Date(time)
        }
        return date
    }

    @TypeConverter
    fun toLong(date: Date?): Long? {
        var long: Long? = null
        if (date != null) {
            long = date.time
        }
        return long
    }

}
