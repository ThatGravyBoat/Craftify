package tech.thatgravyboat.craftify.themes.library

import com.google.gson.GsonBuilder
import tech.thatgravyboat.craftify.themes.ColorSerializer
import tech.thatgravyboat.craftify.themes.Theme
import tech.thatgravyboat.craftify.themes.UrlSerializer
import tech.thatgravyboat.craftify.utils.Utils
import java.awt.Color
import java.net.URL

object LibraryStorage {

    private val GSON = GsonBuilder()
        .registerTypeAdapter(Color::class.java, ColorSerializer)
        .registerTypeAdapter(URL::class.java, UrlSerializer)
        .create()

    private const val JSON_URL = "https://raw.githubusercontent.com/Craftify-Mod/Data/main/themes.json"

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
                    "default",
                    "Default Theme",
                    "ThatGravyBoat",
                    URL("https://files.teamresourceful.com/r/ctWcDL.png"),
                    URL("https://files.teamresourceful.com/r/TeRHEF.png"),
                    "This is the default theme provided by the mod.",
                    Theme()
                )
            )
        )
    }

    private fun download() = runCatching {
        val fromJson = GSON.fromJson(Utils.fetchString(JSON_URL), Array<LibraryTheme>::class.java)
        for (libraryTheme in fromJson) {
            storage[libraryTheme.id] = libraryTheme
        }
    }
}
