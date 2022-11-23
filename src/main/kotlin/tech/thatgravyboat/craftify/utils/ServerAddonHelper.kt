package tech.thatgravyboat.craftify.utils

import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.platform.network.PacketHandler
import tech.thatgravyboat.craftify.platform.network.writeCollection
import tech.thatgravyboat.craftify.platform.network.writeString
import tech.thatgravyboat.jukebox.api.events.EventType
import tech.thatgravyboat.jukebox.api.service.BaseService
import tech.thatgravyboat.jukebox.api.state.State

object ServerAddonHelper {

    private const val PACKET_ID = "music:song"

    fun setupServerAddon(service: BaseService) {
        service.registerListener(EventType.SONG_CHANGE) { event ->
            if (event.state.isPlaying && !event.state.song.type.isAd()) {
                sendSong(event.state)
            }
        }
    }

    private fun sendSong(state: State) {
        if (!isAllowedToSendSong()) return
        PacketHandler.sendPacket(PACKET_ID) { buf ->
            buf.writeString(state.song.title)
            buf.writeCollection(getArtists(state)) { artistBuf, artist ->
                artistBuf.writeString(artist)
            }
        }
    }

    private fun isAllowedToSendSong(): Boolean {
        if (!Config.sendPackets) return false
        if (PacketHandler.isOnLanServer()) return true
        return Config.allowedServers.split("\n").any { it.trim() == PacketHandler.getServerAddress() }
    }

    private fun getArtists(state: State): List<String> {
        if (state.song.artists.size > 2) {
            return state.song.artists.subList(0, 2)
        }
        return state.song.artists
    }
}