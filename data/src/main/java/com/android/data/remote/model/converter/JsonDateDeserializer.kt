package com.android.data.remote.model.converter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.util.*

class JsonDateDeserializer : JsonDeserializer<Date> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date {
        val stringDate = json.asJsonPrimitive.asString
        val longDate = stringDate.substring(6, stringDate.length - 7).toLong()
        return Date(longDate)
    }
}
