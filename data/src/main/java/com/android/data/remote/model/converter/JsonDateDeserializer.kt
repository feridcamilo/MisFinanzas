package com.android.data.remote.model.converter

import com.android.data.utils.StringUtils.Companion.EMPTY
import com.google.gson.*
import java.lang.reflect.Type
import java.util.*

class JsonDateDeserializer : JsonDeserializer<Date>, JsonSerializer<Date> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): Date {
        val date = json.asJsonPrimitive.asString
        val cleanDate = date.replace("/Date(", EMPTY).replace(")/", EMPTY)
        val longDate = cleanDate.substring(0, cleanDate.length - 5).toLong()
        //return DateUtils.getDateTimeToWebService(Date(longDate))
        return Date(longDate)
    }

    @Throws(JsonParseException::class)
    override fun serialize(date: Date, type: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive("/Date(" + date.time + ")/")
    }
}
