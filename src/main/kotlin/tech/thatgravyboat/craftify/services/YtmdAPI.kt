package tech.thatgravyboat.craftify.services

import com.google.gson.Gson
import com.google.gson.JsonObject
import gg.essential.api.utils.Multithreading
import gg.essential.universal.ChatColor
import gg.essential.universal.UChat
import gg.essential.universal.utils.MCClickEventAction
import gg.essential.universal.wrappers.message.UTextComponent
import org.apache.commons.io.IOUtils
import tech.thatgravyboat.craftify.api.Http
import tech.thatgravyboat.craftify.api.MethodType
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.types.PlayerState
import tech.thatgravyboat.craftify.ui.Player
import tech.thatgravyboat.craftify.ui.enums.copying.LinkingMode
import java.net.URI
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

private const val API: String = "http://localhost:9863/query"
private val GSON = Gson()

object YtmdAPI : BaseAPI {

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
        poller = Multithreading.schedule({ pollYtmd() }, 0, 2, TimeUnit.SECONDS)
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

    private fun pollYtmd() {
        if (!Config.hasPassword()) return
        if (Config.modMode != 2) {
            stopPoller()
            return
        }
        Http.call(API, MethodType.GET, Config.ytmdPassword).let {
            val res = IOUtils.toString(it.inputStream, Charsets.UTF_8)
            val data = try { GSON.fromJson(res, JsonObject::class.java) }
            catch (e: Exception) {
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
                data?.let { out -> YtmdPlayerState.parseState(out) }
            } catch (e: Exception) {
                e.printStackTrace()
                pollError()
                null
            }

            lastState?.let { state ->
                if (state.isAd()) {
                    lastState = createBisectAd(state)
                }
            }

            errorCount = 0

            lastState?.let { state ->
                Player.updatePlayer(state)
            }
        }
    }

    override fun changePlayingState(playing: Boolean) {
        if (lastState == null || !Config.hasPassword()) return
        val path: String? = if (playing && !lastState!!.is_playing) "track-play" else if (!playing && lastState!!.is_playing) "track-pause" else null
        path?.let {
            val data = "{\"command\":\"$it\"}"
            Http.call(API, MethodType.POST, Config.ytmdPassword, data, "application/json")
        }
    }

    override fun toggleShuffle(shuffling: Boolean) {
        if (lastState == null || !Config.hasPassword()) return
        val data = "{\"command\":\"player-shuffle\"}"
        Http.call(API, MethodType.POST, Config.ytmdPassword, data, "application/json")
    }

    override fun toggleRepeat(repeating: Boolean) {
        if (lastState == null || !Config.hasPassword()) return
        val repeatState: String? = if (repeating && !lastState!!.isRepeating()) "ALL" else if (!repeating && lastState!!.isRepeating()) "NONE" else null
        repeatState?.let {
            val data = "{\"command\":\"player-repeat\", \"value\":\"$it\"}"
            Http.call(API, MethodType.POST, Config.ytmdPassword, data, "application/json")
        }
    }

    override fun skip(forward: Boolean) {
        val data = "{\"command\":\"${if (forward) "track-next" else "track-previous"}\"}"
        Http.call(API, MethodType.POST, Config.ytmdPassword, data, "application/json")
    }

    override fun setVolume(volume: Int, showNotification: Boolean) {
        val data = "{\"command\":\"player-set-volume\", \"value\":\"$volume\"}"
        Http.call(API, MethodType.POST, Config.ytmdPassword, data, "application/json").let {
            if (showNotification) {
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
}