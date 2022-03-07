package tech.thatgravyboat.craftify.themes.library

import com.google.gson.GsonBuilder
import gg.essential.api.utils.WebUtil
import tech.thatgravyboat.craftify.themes.ColorSerializer
import tech.thatgravyboat.craftify.themes.Theme
import tech.thatgravyboat.craftify.themes.UrlSerializer
import java.awt.Color
import java.net.URL

object LibraryStorage {

    private val GSON = GsonBuilder()
        .registerTypeAdapter(Color::class.java, ColorSerializer)
        .registerTypeAdapter(URL::class.java, UrlSerializer)
        .create()

    private const val JSON_URL = "https://raw.githubusercontent.com/ThatGravyBoat/craftify-data/main/themes.json"

    private var storage: MutableMap<String, LibraryTheme> = mutableMapOf()

    init {
        refresh()
    }

    fun getTheme(id: String): LibraryTheme {
        return storage.getOrDefault(id, storage["default"]!!)
    }

    fun getThemes(): Collection<LibraryTheme> {
        return storage.values
    }

    fun refresh() {
        createMap()
        download()
    }

    private fun createMap() {
        storage = mutableMapOf(
            Pair(
                "default",
                LibraryTheme(
                    "default", "Default Theme", "ThatGravyBoat",
                    URL("https://i.imgur.com/JQdBt2K.png"), URL("https://i.imgur.com/oJxN5Tm.png"),
                    "This is the default theme provided by the mod.", Theme()
                )
            )
        )
    }

    private fun download() {
        try {
            val fromJson = GSON.fromJson(WebUtil.fetchString(JSON_URL), Array<LibraryTheme>::class.java)
            for (libraryTheme in fromJson) {
                storage[libraryTheme.id] = libraryTheme
            }
        } catch (ignored: Exception) {
            // Ignore when could not download
        }
    }
}
