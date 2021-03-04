package com.android.data.local.model.converters

import com.android.domain.model.Movement
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class MovementConverter {

    fun stringToMovement(value: String): Movement {
        val gson = Gson()
        val type: Type = object : TypeToken<Movement>() {}.type
        return gson.fromJson(value, type)
    }

    fun movementToString(value: Movement): String {
        val gson = Gson()
        return gson.toJson(value)
    }
}
