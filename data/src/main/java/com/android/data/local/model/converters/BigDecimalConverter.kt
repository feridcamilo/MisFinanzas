package com.android.data.local.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.math.BigDecimal

class BigDecimalConverter {
    @TypeConverter
    fun stringToBigDecimal(value: String): BigDecimal {
        val gson = Gson()
        val type: Type = object : TypeToken<BigDecimal>() {}.type
        return gson.fromJson<BigDecimal>(value, type)
    }

    @TypeConverter
    fun bigDecimalTypeToString(value: BigDecimal): String {
        val gson = Gson()
        return gson.toJson(value)
    }
}
