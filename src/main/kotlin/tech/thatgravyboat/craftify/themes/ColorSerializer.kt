package tech.thatgravyboat.craftify.themes

import com.google.gson.*
import java.awt.Color
import java.lang.reflect.Type

object ColorSerializer : JsonSerializer<Color>, JsonDeserializer<Color> {

    override fun serialize(src: Color?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.rgb)
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Color {
        return json?.asInt?.let { Color(it, true) } ?: Color.WHITE
    }
}
