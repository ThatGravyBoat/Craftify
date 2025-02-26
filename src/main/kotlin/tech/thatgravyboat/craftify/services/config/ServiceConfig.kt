package tech.thatgravyboat.craftify.services.config

import com.google.gson.JsonObject
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.*
import tech.thatgravyboat.craftify.config.ReadWritePropertyValue
import tech.thatgravyboat.craftify.utils.readJson
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlin.reflect.KMutableProperty0

private val EMPTY_CONFIG = object : Vigilant(File("")) { }

interface ServiceConfig {

    fun load(data: JsonObject)
    fun save(data: JsonObject)

    fun getEntries(): List<CategoryItem>

    companion object {

        private val file = Path(System.getProperty("user.home"), ".craftify")
        private val configs = mapOf(
            "spotify" to SpotifyServiceConfig,
            "youtube" to YoutubeServiceConfig,
            "beefweb" to BeefwebServiceConfig,
        )
        private val json = JsonObject()

        fun load() {
            val json = file.takeIf { it.exists() }?.runCatching { this.readText().readJson() }?.getOrNull() ?: JsonObject()
            for ((id, config) in configs) {
                val data = this.json.getAsJsonObject(id) ?: JsonObject()
                val json = json.getAsJsonObject(id) ?: JsonObject()
                for ((key, value) in json.entrySet()) {
                    data.add(key, value)
                }
                config.load(data)
                this.json.add(id, data)
            }
        }

        fun save() {
            for ((id, config) in configs) {
                val data = this.json.getAsJsonObject(id) ?: JsonObject()
                config.save(data)
                this.json.add(id, data)
            }
            this.file.writeText(this.json.toString())
        }

        fun get(id: String?): List<CategoryItem> = configs[id]?.getEntries() ?: listOf()
    }

}

private fun MutableList<CategoryItem>.property(attributes: PropertyAttributesExt, value: PropertyValue) =
    PropertyItem(PropertyData(attributes, value, EMPTY_CONFIG), "")

fun MutableList<CategoryItem>.divider(title: String, desc: String) = DividerItem(title, desc)
fun MutableList<CategoryItem>.button(name: String, desc: String, action: () -> Unit) = property(
    PropertyAttributesExt(type = PropertyType.BUTTON, name = name, category = "", description = desc),
    KFunctionBackedPropertyValue(action),
)
fun MutableList<CategoryItem>.password(name: String, desc: String, field: KMutableProperty0<String>) = property(
    PropertyAttributesExt(type = PropertyType.TEXT, name = name, category = "", description = desc, protected = true),
    ReadWritePropertyValue(field::get, field::setFromAny),
)
fun MutableList<CategoryItem>.number(name: String, desc: String, field: KMutableProperty0<Int>) = property(
    PropertyAttributesExt(type = PropertyType.NUMBER, name = name, category = "", description = desc),
    ReadWritePropertyValue(field::get, field::setFromAny),
)

private inline fun <reified T> KMutableProperty0<T>.setFromAny(value: Any?) {
    if (value is T) this.set(value)
}