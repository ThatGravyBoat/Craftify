package tech.thatgravyboat.craftify.extensions

import com.google.gson.JsonObject

fun JsonObject.getAsBoolean(key: String, default: Boolean = false): Boolean {
    return try {
        if (this.has(key)) this.get(key).asBoolean else default
    }catch (e: Exception) {
        default
    }
}

fun JsonObject.getAsFloat(key: String, default: Float = 0f): Float {
    return try {
        if (this.has(key)) this.get(key).asFloat else default
    }catch (e: Exception) {
        default
    }
}

fun JsonObject.getAsDouble(key: String, default: Double = 0.0): Double {
    return try {
        if (this.has(key)) this.get(key).asDouble else default
    }catch (e: Exception) {
        default
    }
}

fun JsonObject.getAsInt(key: String, default: Int = 0): Int {
    return try {
        if (this.has(key)) this.get(key).asInt else default
    }catch (e: Exception) {
        default
    }
}

fun JsonObject.getAsLong(key: String, default: Long = 0): Long {
    return try {
        if (this.has(key)) this.get(key).asLong else default
    }catch (e: Exception) {
        default
    }
}

fun JsonObject.getAsString(key: String, default: String = ""): String {
    return try {
        if (this.has(key)) this.get(key).asString else default
    }catch (e: Exception) {
        default
    }
}