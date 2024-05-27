package tech.thatgravyboat.craftify.services.ads

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import tech.thatgravyboat.craftify.services.update.UpdateVersion
import tech.thatgravyboat.craftify.services.update.UpdateVersionSerializer
import tech.thatgravyboat.craftify.utils.Utils
import tech.thatgravyboat.jukebox.api.state.State

object AdManager {

    private val GSON = GsonBuilder()
        .registerTypeAdapter(UpdateVersion::class.java, UpdateVersionSerializer)
        .create()

    private const val JSON_URL = "https://raw.githubusercontent.com/Craftify-Mod/Data/main/ads.json"

    private val ads: MutableMap<String, Advertisment> = mutableMapOf(
        "bisect" to Advertisment(
            "Bisect Hosting Partner",
            "use code '§agravy§r'",
            "https://www.bisecthosting.com/images/logos/logo-app.png",
            "https://www.bisecthosting.com/gravy"
        )
    )
    private var adId: String? = null

    fun load() {
        try {
            val json = GSON.fromJson(Utils.fetchString(JSON_URL), JsonObject::class.java)
            for (entry in json.entrySet()) {
                if (entry.value !is JsonObject) continue
                ads[entry.key] = GSON.fromJson(entry.value, Advertisment::class.java)
            }
        } catch (ignored: Exception) {
            // Ignore when could not download
        }
    }

    fun changeAd() {
        adId = null
    }

    fun getAdState(state: State): State {
        if (adId == null) {
            adId = ads.keys.random()
        }
        return ads[adId]!!.toState(state)
    }

}