package tech.thatgravyboat.craftify.services

import com.google.gson.Gson
import com.google.gson.JsonObject
import gg.essential.universal.ChatColor
import gg.essential.universal.UChat
import gg.essential.universal.wrappers.UPlayer
import org.apache.commons.io.IOUtils
import tech.thatgravyboat.craftify.Initializer
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.ui.Player
import tech.thatgravyboat.craftify.utils.EssentialUtils
import tech.thatgravyboat.craftify.utils.ServerAddonHelper
import tech.thatgravyboat.craftify.utils.Utils
import tech.thatgravyboat.jukebox.api.events.EventType
import tech.thatgravyboat.jukebox.api.events.callbacks.SongChangeEvent
import tech.thatgravyboat.jukebox.api.events.callbacks.UpdateEvent
import tech.thatgravyboat.jukebox.api.events.callbacks.VolumeChangeEvent
import tech.thatgravyboat.jukebox.api.service.BaseService
import tech.thatgravyboat.jukebox.api.service.ServiceFunction
import tech.thatgravyboat.jukebox.impl.spotify.SpotifyService
import java.nio.charset.StandardCharsets

object ServiceHelper {

    private const val AUTH_API: String = "https://craftify.thatgravyboat.tech/api/v1/public/auth"
    private val badTokens = mutableSetOf<String>()
    private val essentialPackets = System.getProperty("craftify.essentialFakePackets", "false") == "true"
    private val GSON = Gson()
    private var lastFailedLogin = 0L

    private val songUpdate: (UpdateEvent) -> Unit = { Player.updatePlayer(it.state) }
    private val songChange: (SongChangeEvent) -> Unit = { Player.changeSong(it.state) }
    private val volumeChange: (VolumeChangeEvent) -> Unit = {
        if (it.shouldNotify && Utils.isEssentialInstalled) {
            showVolumeNotification(it.volume)
        }
    }

    fun doesSupport(function: ServiceFunction): Boolean
        = Initializer.getAPI()?.getFunctions()?.contains(function) ?: false

    fun BaseService.setup() {
        registerListener(EventType.UPDATE, songUpdate)
        registerListener(EventType.SONG_CHANGE, songChange)
        registerListener(EventType.VOLUME_CHANGE, volumeChange)
        if (Config.sendPackets) {
            ServerAddonHelper.setupServerAddon(this)
        }
        if (essentialPackets && Utils.isEssentialInstalled) {
            EssentialUtils.setupServerAddon(this)
        }
    }

    fun BaseService.close() {
        unregisterListener(EventType.UPDATE, songUpdate)
        unregisterListener(EventType.SONG_CHANGE, songChange)
        unregisterListener(EventType.VOLUME_CHANGE, volumeChange)
        if (Config.sendPackets) {
            ServerAddonHelper.closeServerAddon(this)
        }
        if (essentialPackets && Utils.isEssentialInstalled) {
            EssentialUtils.closeServerAddon(this)
        }
    }

    fun setupSpotify(service: BaseService) {
        if (service is SpotifyService) {
            service.registerListener(EventType.SERVICE_UNAUTHORIZED) {
                Config.optionalRefresh()?.let {
                    loginToSpotify("refresh", it)
                    service.token = Config.token
                    service.restart()
                }
            }
        }
    }

    fun loginToSpotify(type: String, code: String) {
        if (System.currentTimeMillis() - lastFailedLogin < 10000) return
        if (badTokens.contains(code)) return
        var failed = false
        var reason = "Unknown error"
        try {
            Utils.login("$AUTH_API?type=$type&code=$code")?.let { response ->
                val data = try { GSON.fromJson(IOUtils.toString(response.inputStream, StandardCharsets.UTF_8), JsonObject::class.java) } catch (e: Exception) { null }
                if (response.success()) {
                    if (data?.get("success")?.asBoolean == true) {
                        val refreshToken = data.getOrNull("refresh_token")
                        val accessToken = data.getOrNull("access_token")
                        if (accessToken != null) {
                            Config.token = accessToken
                            Config.refreshToken = if (type != "refresh" && refreshToken == null) "" else refreshToken ?: Config.refreshToken
                            Config.musicService = "spotify"
                            Config.markDirty()
                            Config.writeData()
                        } else {
                            failed = true
                        }
                    } else {
                        failed = true
                    }
                } else {
                    failed = true
                }

                if (failed) {
                    reason = data?.getOrNull("reason") ?: "Unknown error"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            failed = true
            reason = "Check the console for more information"
        }

        if (failed && lastFailedLogin < System.currentTimeMillis() - 600000) {
            if (UPlayer.hasPlayer()) {
                UChat.chat("${ChatColor.RED}Craftify > ${ChatColor.GRAY}Failed to login to Spotify: ${reason}, please try logging in again.")
            } else {
                println("Failed to login to Spotify: $reason, please try logging in again.")
            }
            lastFailedLogin = System.currentTimeMillis()
            badTokens.add(code)
        }
    }

    private fun JsonObject.getOrNull(key: String): String? {
        return if (has(key)) get(key).asString else null
    }

    private fun showVolumeNotification(volume: Int) {
        val image = when {
            volume <= 0 -> "https://images.teamresourceful.com/u/cwD8Hn.png"
            volume <= 30 -> "https://images.teamresourceful.com/u/xtBaQY.png"
            volume <= 70 -> "https://images.teamresourceful.com/u/DWDtyo.png"
            else -> "https://images.teamresourceful.com/u/FKSB9b.png"
        }
        EssentialUtils.sendNotification(
            title = "Craftify",
            message = "The volume has been set to $volume%",
            image = image,
        )
    }
}