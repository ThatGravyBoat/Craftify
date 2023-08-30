package tech.thatgravyboat.craftify.services.update

import com.google.gson.GsonBuilder
import gg.essential.universal.UChat
import tech.thatgravyboat.craftify.utils.Utils

object Updater {

    private val GSON = GsonBuilder()
        .registerTypeAdapter(UpdateVersion::class.java, UpdateVersionSerializer)
        .create()

    private const val JSON_URL = "https://raw.githubusercontent.com/Craftify-Mod/Data/main/messages.json"

    private val VERSION = UpdateVersion("@VER@")
    private var latestMessage: UpdateMessage? = null
    private var showMessage = true

    fun check() {
        try {
            val fromJson = GSON.fromJson(Utils.fetchString(JSON_URL), Array<UpdateMessage>::class.java)
            for (message in fromJson) {
                if (latestMessage == null || message.version > latestMessage!!.version) {
                    latestMessage = message
                }
            }
        } catch (ignored: Exception) {
            // Ignore when could not download
        }
    }

    fun getLatestMessage(): UpdateMessage? {
        return latestMessage
    }

    fun hasUpdate(): Boolean {
        return latestMessage != null && latestMessage!!.version > VERSION
    }

    fun showMessage() {
        if (!showMessage) return
        showMessage = false
        UChat.chat("")
        UChat.chat("\u00A77----------[\u00A7aCraftify\u00A77]----------")
        UChat.chat("\u00A76There is a new update available!")
        UChat.chat("\u00A76Current version: \u00A7a${VERSION.value}")
        UChat.chat("\u00A76Latest version: \u00A7a${latestMessage!!.version.value}")
        UChat.chat("\u00A76Message:")
        latestMessage!!.message.lines().forEach { UChat.chat(" §a$it") }
        UChat.chat("\u00A77-----------------------------")
        UChat.chat("")
    }
}