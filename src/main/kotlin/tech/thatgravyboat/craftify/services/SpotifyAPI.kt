package tech.thatgravyboat.craftify.services

import com.google.gson.Gson
import com.google.gson.JsonObject
import gg.essential.api.utils.Multithreading
import gg.essential.universal.ChatColor
import gg.essential.universal.UChat
import gg.essential.universal.utils.MCClickEventAction
import gg.essential.universal.wrappers.message.UTextComponent
import org.apache.commons.io.IOUtils
import tech.thatgravyboat.craftify.Config
import tech.thatgravyboat.craftify.api.Http
import tech.thatgravyboat.craftify.api.MethodType
import tech.thatgravyboat.craftify.api.Paths
import tech.thatgravyboat.craftify.types.PlayerState
import tech.thatgravyboat.craftify.ui.Player
import tech.thatgravyboat.craftify.ui.enums.copying.LinkingMode
import java.net.URI
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

private const val AUTH_API: String = "https://craftify-api.vercel.app/api/auth"
private val GSON = Gson()

object SpotifyAPI : BaseAPI {

    private var poller: ScheduledFuture<*>? = null
    private var lastState: PlayerState? = null
    private var lastStateData: JsonObject? = null
    private var errorCount = 0

    override fun getState(): PlayerState? {
        return lastState
    }

    override fun restartPoller() {
        if (!stopPoller()) {
            Multithreading.schedule({ startPoller() }, 2, TimeUnit.SECONDS)
        } else {
            startPoller()
        }
    }

    override fun startPoller() {
        poller = Multithreading.schedule({ pollSpotify() }, 0, 2, TimeUnit.SECONDS)
    }

    override fun stopPoller(): Boolean {
        return poller?.cancel(false) ?: false
    }

    private fun pollError() {
        if (errorCount == 10) {
            stopPoller()
        }
        errorCount++
    }

    private fun pollSpotify() {
        if (!Config.hasToken()) return
        if (Config.modMode != 1) {
            stopPoller()
            return
        }
        fetch(Paths.PLAYER)?.let { res ->
            val data = try {
                GSON.fromJson(res, JsonObject::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                pollError()
                null
            }

            if (data?.has("error") == true) {
                println(data)
                return pollError()
            }

            lastStateData = data
            lastState = try {
                GSON.fromJson(res, PlayerState::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                pollError()
                null
            }

            errorCount = 0

            lastState?.let { state ->
                Player.updatePlayer(state)
            }
        }
    }

    override fun changePlayingState(playing: Boolean) {
        if (lastState == null || !Config.hasToken()) return
        val path: Paths? = if (playing && !lastState!!.is_playing) Paths.PLAY else if (!playing && lastState!!.is_playing) Paths.PAUSE else null
        path?.let { pathIt ->
            callCloseGetCode(pathIt, "")?.let {
                if (it == 401) regenToken()
            }
        }
    }

    override fun toggleShuffle(shuffling: Boolean) {
        if (lastState == null || !Config.hasToken()) return
        val shuffleState: Boolean? = if (shuffling && !lastState!!.isShuffling()) true else if (!shuffling && lastState!!.isShuffling()) false else null
        shuffleState?.let { state ->
            callCloseGetCode(Paths.SHUFFLE, "", mapOf(Pair("state", state.toString())))?.let {
                if (it == 401) regenToken()
            }
        }
    }

    override fun toggleRepeat(repeating: Boolean) {
        if (lastState == null || !Config.hasToken()) return
        val repeatState: String? = if (repeating && !lastState!!.isRepeating()) "context" else if (!repeating && lastState!!.isRepeating()) "off" else null
        repeatState?.let { state ->
            callCloseGetCode(Paths.REPEAT, "", mapOf(Pair("state", state)))?.let {
                if (it == 401) regenToken()
            }
        }
    }

    override fun skip(forward: Boolean) {
        val path: Paths = if (forward) Paths.NEXT else Paths.PREV
        callCloseGetCode(path, "")?.let {
            if (it == 401) regenToken()
        }
    }

    override fun setVolume(volume: Int, showNotification: Boolean) {
        callCloseGetCode(Paths.SETVOLUME, "", mapOf(Pair("volume_percent", "$volume")))?.let {
            if (it == 401) regenToken()
            else if (showNotification) {
                showVolumeNotification(volume)
            }
        }
    }

    override fun openTrack() {
        lastState?.item?.external_urls?.spotify?.let {
            val linkingMode = LinkingMode.values()[Config.linkMode]
            if (!linkingMode.copy(URI(it))) {
                val component = UTextComponent("${ChatColor.GREEN}Craftify > ${ChatColor.GRAY} $it")
                component.setClick(MCClickEventAction.OPEN_URL, it)
                UChat.chat(component)
            }
        }
    }

    private fun regenToken() {
        Config.optionalRefresh()?.let { login("refresh", it) }
        pollError() // So if refresh fails it will stop eventually.
    }

    fun login(type: String, code: String) {
        try {
            Http.call(
                AUTH_API,
                MethodType.POST,
                body = "{\"type\": \"$type\", \"code\": \"$code\"}",
                contentType = "application/json"
            ).let { res ->
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
                        if (type != "refresh") restartPoller()
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

    private fun fetch(path: Paths, body: String? = null, params: Map<String, String>? = null): String? {
        try {
            Http.call(path, Config.token, body, params)?.let { res ->
                if (res.responseCode == 401) {
                    regenToken()
                    return null
                }
                res.inputStream?.let { stream ->
                    return IOUtils.toString(stream, Charsets.UTF_8)
                }
                return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun callCloseGetCode(paths: Paths, body: String? = null, params: Map<String, String>? = null): Int? {
        Http.call(paths, Config.token, body, params)?.let {
            it.inputStream?.close()
            return it.responseCode
        }
        return null
    }
}