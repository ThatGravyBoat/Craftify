package tech.thatgravyboat.craftify.services

import com.google.gson.Gson
import com.google.gson.JsonObject
import gg.essential.universal.ChatColor
import gg.essential.universal.UChat
import gg.essential.universal.wrappers.UPlayer
import org.apache.commons.io.IOUtils
import tech.thatgravyboat.craftify.Initializer
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.services.addons.Addon
import tech.thatgravyboat.craftify.services.config.ServiceConfig
import tech.thatgravyboat.craftify.services.config.SpotifyServiceConfig
import tech.thatgravyboat.craftify.utils.Utils
import tech.thatgravyboat.craftify.utils.getString
import tech.thatgravyboat.jukebox.api.events.EventType
import tech.thatgravyboat.jukebox.api.service.BaseService
import tech.thatgravyboat.jukebox.api.service.ServiceFunction
import tech.thatgravyboat.jukebox.impl.spotify.SpotifyService
import java.nio.charset.StandardCharsets

object ServiceHelper {

    private const val AUTH_API: String = "https://craftify.thatgravyboat.tech/api/v1/public/auth"
    private val badTokens = mutableSetOf<String>()
    private val GSON = Gson()
    private var lastFailedLogin = 0L

    fun doesSupport(function: ServiceFunction): Boolean
        = Initializer.getAPI()?.getFunctions()?.contains(function) ?: false

    fun BaseService.setup() {
        Addon.addons.forEach { addon -> addon.setup(this) }
    }

    fun BaseService.close() {
        Addon.addons.forEach { addon -> addon.close(this) }
    }

    fun setupSpotify(service: BaseService) {
        if (service is SpotifyService) {
            service.registerListener(EventType.SERVICE_UNAUTHORIZED) {
                SpotifyServiceConfig.refresh.takeUnless(String::isBlank)?.let {
                    loginToSpotify("refresh", it)
                    service.token = SpotifyServiceConfig.auth
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
                        val refreshToken = data.getString("refresh_token")
                        val accessToken = data.getString("access_token")
                        if (accessToken != null) {
                            SpotifyServiceConfig.auth = accessToken
                            SpotifyServiceConfig.refresh = refreshToken ?: SpotifyServiceConfig.refresh
                            ServiceConfig.save()

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
                    reason = data?.getString("reason") ?: "Unknown error"
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
}