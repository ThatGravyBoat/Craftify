package tech.thatgravyboat.craftify.services.update

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

data class UpdateVersion(val value: String) : Comparable<UpdateVersion> {

    private fun getValues(): Triple<Int, Int, Int> {
        val (major, minor, patch) = value.split(".")
        return Triple(major.toInt(), minor.toInt(), patch.toInt())
    }

    override fun compareTo(other: UpdateVersion): Int {
        if (!value.matches(VERSION_REGEX)) return -1
        if (!other.value.matches(VERSION_REGEX)) return 1
        val (major, minor, patch) = getValues()
        val (otherMajor, otherMinor, otherPatch) = other.getValues()
        return when {
            major > otherMajor -> 1
            major < otherMajor -> -1
            minor > otherMinor -> 1
            minor < otherMinor -> -1
            patch > otherPatch -> 1
            patch < otherPatch -> -1
            else -> 0
        }
    }

    companion object {
        private val VERSION_REGEX = Regex("^(\\d+)\\.(\\d+)\\.(\\d+)$")
    }
}

object UpdateVersionSerializer : JsonDeserializer<UpdateVersion> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): UpdateVersion {
        return json?.asString?.let { UpdateVersion(it) } ?: throw Exception("JSON CONTAINS INVALID VERSION")
    }
}