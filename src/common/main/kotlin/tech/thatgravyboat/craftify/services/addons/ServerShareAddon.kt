package tech.thatgravyboat.craftify.services.addons

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.platform.McClient
import tech.thatgravyboat.craftify.platform.ServerboundSongPacket
import tech.thatgravyboat.jukebox.api.events.EventType
import tech.thatgravyboat.jukebox.api.events.callbacks.SongChangeEvent
import tech.thatgravyboat.jukebox.api.service.BaseService
import java.util.*

object ServerShareAddon : Addon {

    var servers: Set<String>
        get() = Config.allowedServers.split("\n").map(String::trim).filter(String::isNotEmpty).toSet()
        set(value) {
            Config.allowedServers = value.joinToString("\n")
        }

    private val enabled: Boolean get() {
        if (!Config.sendPackets) return false
        if (McClient.isLanServer) return true
        val server = McClient.serverIp ?: return false
        return server in servers
    }

    private val handler: (SongChangeEvent) -> Unit = {
        val song = it.state.song
        if (it.state.isPlaying && !song.type.isAd() && this.enabled) {
            ClientPlayNetworking.send(ServerboundSongPacket(
                it.state.song.title,
                it.state.song.artists.take(2),
                Optional.ofNullable(it.state.song.url.takeIf(String::isNotEmpty))
            ))
        }
    }

    override fun setup(service: BaseService) {
        service.registerListener(EventType.SONG_CHANGE, handler)
    }

    override fun close(service: BaseService) {
        service.unregisterListener(EventType.SONG_CHANGE, handler)
    }
}