package tech.thatgravyboat.craftify.services.addons

import gg.essential.universal.UChat
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.platform.McClient
import tech.thatgravyboat.craftify.services.ads.AdManager
import tech.thatgravyboat.craftify.ui.Player
import tech.thatgravyboat.jukebox.api.events.EventType
import tech.thatgravyboat.jukebox.api.events.callbacks.SongChangeEvent
import tech.thatgravyboat.jukebox.api.events.callbacks.UpdateEvent
import tech.thatgravyboat.jukebox.api.service.BaseService
import tech.thatgravyboat.jukebox.api.state.PlayingType

object PlayerAddon : Addon {

    private val songUpdate: (UpdateEvent) -> Unit = {
        if (Config.announcementEnabled && Player.state?.isSame(it.state) != true) {
            McClient.run {
                UChat.chat(
                    Config.announcementMessage
                        .let(UChat::addColor)
                        .replace("\${song}", it.state.song.title)
                        .replace("\${artists}", it.state.song.artists.joinToString( ", "))
                        .replace("\${artist}", it.state.song.artists.getOrElse(0) { "" })
                )
            }
        }

        Player.state = if (it.state.song.type == PlayingType.AD) {
            AdManager.getAdState(it.state)
        } else {
            it.state
        }
    }
    private val songChange: (SongChangeEvent) -> Unit = {
        AdManager.changeAd()
    }

    override fun setup(service: BaseService) {
        service.registerListener(EventType.UPDATE, songUpdate)
        service.registerListener(EventType.SONG_CHANGE, songChange)
    }

    override fun close(service: BaseService) {
        service.unregisterListener(EventType.UPDATE, songUpdate)
        service.unregisterListener(EventType.SONG_CHANGE, songChange)
    }
}