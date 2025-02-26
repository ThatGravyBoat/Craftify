package tech.thatgravyboat.craftify.services.addons

import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.platform.network.PacketHandler
import tech.thatgravyboat.craftify.platform.network.writeCollection
import tech.thatgravyboat.craftify.platform.network.writeString
import tech.thatgravyboat.jukebox.api.events.EventType
import tech.thatgravyboat.jukebox.api.events.callbacks.SongChangeEvent
import tech.thatgravyboat.jukebox.api.service.BaseService

private const val PACKET_ID = "music:song"

object ServerShareAddon : Addon {

    private val enabled: Boolean get() {
        if (!Config.sendPackets) return false
        if (PacketHandler.isOnLanServer()) return true
        val server = PacketHandler.getServerAddress().takeIf(String::isNotBlank) ?: return false
        return Config.allowedServers.split("\n").any { it.trim() == server }
    }

    private val handler: (SongChangeEvent) -> Unit = {
        val song = it.state.song
        if (it.state.isPlaying && !song.type.isAd() && this.enabled) {
            PacketHandler.sendPacket(PACKET_ID) { buf ->
                buf.writeString(it.state.song.title)
                buf.writeCollection(it.state.song.artists.take(2)) { artistBuf, artist ->
                    artistBuf.writeString(artist)
                }
            }
        }
    }

    override fun setup(service: BaseService) {
        service.registerListener(EventType.SONG_CHANGE, handler)
    }

    override fun close(service: BaseService) {
        service.unregisterListener(EventType.SONG_CHANGE, handler)
    }
}