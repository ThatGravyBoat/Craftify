package tech.thatgravyboat.craftify.services

import com.google.gson.Gson
import org.apache.commons.io.IOUtils
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.ui.Player
import tech.thatgravyboat.craftify.utils.EssentialApiHelper
import tech.thatgravyboat.craftify.utils.ServerAddonHelper
import tech.thatgravyboat.jukebox.api.events.EventType
import tech.thatgravyboat.jukebox.api.service.BaseService
import tech.thatgravyboat.jukebox.api.state.PlayingType
import tech.thatgravyboat.jukebox.api.state.Song
import tech.thatgravyboat.jukebox.api.state.SongState
import tech.thatgravyboat.jukebox.api.state.State
import tech.thatgravyboat.jukebox.impl.spotify.SpotifyService

object ServiceHelper {

    private const val AUTH_API: String = "https://craftify-api.vercel.app/api/auth"
    private val GSON = Gson()

    fun BaseService.setup() {
        registerListener(EventType.UPDATE) {
            Player.updatePlayer(it.state)
        }
        registerListener(EventType.SONG_CHANGE) {
            Player.announceSong(it.state)
        }
        registerListener(EventType.VOLUME_CHANGE) {
            if (it.shouldNotify) showVolumeNotification(it.volume)
        }
        if (Config.thisIsForTestingPacketsDoNotTurnOn) {
            ServerAddonHelper.setupServerAddon(this)
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
        try {
            Http.post(
                AUTH_API,
                body = "{\"type\": \"$type\", \"code\": \"$code\"}",
                contentType = "application/json"
            )?.let { res ->
                res.inputStream?.let { stream ->
                    val data = try {
                        GSON.fromJson(IOUtils.toString(stream, Charsets.UTF_8), TokenData::class.java)
                    } catch (e: Exception) {
                        null
                    }
                    data?.let {
                        Config.token = data.access_token ?: ""
                        Config.refreshToken = if (type != "refresh" && data.refresh_token == null) "" else data.refresh_token ?: Config.refreshToken
                        Config.modMode = 1
                        Config.markDirty()
                        Config.writeData()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private data class TokenData(
        val success: Boolean = false,
        val access_token: String? = null,
        val refresh_token: String? = null
    )

    private fun showVolumeNotification(volume: Int) {
        val image = when {
            volume <= 0 -> "https://i.imgur.com/v2a3Z8n.png"
            volume <= 30 -> "https://i.imgur.com/8L4av1O.png"
            volume <= 70 -> "https://i.imgur.com/tGJKxRr.png"
            else -> "https://i.imgur.com/1Ay43hi.png"
        }
        EssentialApiHelper.sendNotification(
            title = "Craftify",
            message = "The volume has been set to $volume%",
            image = image,
        )
    }

    fun createBisectAd(state: State) = State(
        state.player,
        Song(
            "Bisect Hosting Partner",
            listOf("use code '&agravy&r'"),
            "https://www.bisecthosting.com/images/logos/logo-app.png",
            "https://www.bisecthosting.com/gravy",
            PlayingType.AD
        ),
        SongState(0, 0, false)
    )
}