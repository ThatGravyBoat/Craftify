package tech.thatgravyboat.craftify.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import gg.essential.vigilance.Vigilant.CategoryPropertyBuilder
import gg.essential.vigilance.data.PropertyInfo
import gg.essential.vigilance.data.PropertyType
import tech.thatgravyboat.craftify.config.ReadWritePropertyValue
import kotlin.reflect.KMutableProperty0

private val gson = GsonBuilder().setPrettyPrinting().create()

fun CategoryPropertyBuilder.string(field: KMutableProperty0<String>, name: String, description: String) {
    text(field, name, description)
}

fun CategoryPropertyBuilder.boolean(field: KMutableProperty0<Boolean>, name: String, description: String) {
    switch(field, name, description)
}

inline fun <reified T : PropertyInfo> CategoryPropertyBuilder.prop(field: KMutableProperty0<*>, name: String, description: String) {
    custom(field, T::class, name, description)
}

inline fun <reified T : Enum<T>> CategoryPropertyBuilder.enum(
    field: KMutableProperty0<T>,
    name: String,
    description: String,
    crossinline onLoad: ((T) -> Unit) = {  }
) {
    val property = ReadWritePropertyValue({ field.get().ordinal }, { runCatching {
        val enum = enumValues<T>()[it as Int]
        field.set(enum)
        onLoad(enum)
    } })
    val options = enumValues<T>().map(Any::toString)
    property<Int>(property, PropertyType.SELECTOR, name, description, options = options)
}

fun JsonObject.getString(key: String): String? = this.get(key)?.takeIf { it is JsonPrimitive && it.isString }?.asString
fun JsonObject.getInt(key: String): Int? = this.get(key)?.takeIf { it is JsonPrimitive && it.isNumber }?.asInt
fun JsonObject.getBoolean(key: String): Boolean? = this.get(key)?.takeIf { it is JsonPrimitive && it.isBoolean }?.asBoolean

fun String.readJson(): JsonObject = gson.fromJson<JsonObject>(this, JsonObject::class.java)