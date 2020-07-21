package com.android.data.remote.model.converter

import com.android.data.UserSesion
import com.google.gson.*
import java.lang.reflect.Type
import java.util.*

class JsonDateDeserializer : JsonDeserializer<Date>, JsonSerializer<Date> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): Date {
        val date = json.asJsonPrimitive.asString
        val cleanDate = date.replace("/Date(", "").replace(")/", "")
        val longDate = cleanDate.substring(0, cleanDate.length - 5).toLong()
        /* get the GMT from the JSONDate
        var strTimeZone = cleanDate.substring(cleanDate.length - 5, cleanDate.length)
        strTimeZone =  strTimeZone.substring(0, 3) + ":" + strTimeZone.substring(3, strTimeZone.length)
        val timeZone = TimeZone.getTimeZone("GMT" + strTimeZone)
        */
        return Date(longDate + UserSesion.getTimeZone().getOffset(longDate))
    }

    @Throws(JsonParseException::class)
    override fun serialize(date: Date, type: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive("/Date(" + date.time + ")/")
    }
}
