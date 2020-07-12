package com.android.data.local.model.converters

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        var date: Date? = null
        if (timestamp != null) {
            date = Date(timestamp)
        }
        return date
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        var long: Long? = null
        if (date != null) {
            long = date.time
        }
        return long
    }
}
