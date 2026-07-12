package tech.thatgravyboat.craftify.themes

import com.google.gson.*
import java.lang.reflect.Type
import java.net.URL

object UrlSerializer : JsonDeserializer<URL> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): URL {
        return json?.asString?.let { URL(it) } ?: throw JsonSyntaxException("JSON CONTAINS INVALID URL")
    }
}
