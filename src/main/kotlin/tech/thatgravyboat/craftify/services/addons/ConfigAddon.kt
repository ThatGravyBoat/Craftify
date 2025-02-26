package tech.thatgravyboat.craftify.services.addons

import gg.essential.universal.UChat
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.jukebox.api.events.EventType
import tech.thatgravyboat.jukebox.api.events.callbacks.SongChangeEvent
import tech.thatgravyboat.jukebox.api.service.BaseService

object ConfigAddon : Addon {

    private val annouceSong: (SongChangeEvent) -> Unit = {
        if (Config.announcementEnabled) {
            UChat.chat(
                Config.announcementMessage
                    .replace("\${song}", it.state.song.title)
                    .replace("\${artists}", it.state.song.artists.joinToString( ", "))
                    .replace("\${artist}", it.state.song.artists.getOrElse(0) { "" })
            )
        }
    }


    override fun setup(service: BaseService) {
        service.registerListener(EventType.SONG_CHANGE, annouceSong)
    }

    override fun close(service: BaseService) {
        service.unregisterListener(EventType.SONG_CHANGE, annouceSong)
    }
}