package com.android.data.local.model.converters

import androidx.room.TypeConverter
import com.android.data.remote.model.MovementType
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class MovementTypeConverter {
    @TypeConverter
    fun stringToMovementType(value: String): MovementType {
        val gson = Gson()
        val type: Type = object : TypeToken<MovementType>() {}.type
        return gson.fromJson<MovementType>(value, type)
    }

    @TypeConverter
    fun movementTypeToString(value: MovementType): String {
        val gson = Gson()
        return gson.toJson(value)
    }
}
