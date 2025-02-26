package tech.thatgravyboat.craftify.services.addons

import tech.thatgravyboat.craftify.ui.Player
import tech.thatgravyboat.jukebox.api.events.EventType
import tech.thatgravyboat.jukebox.api.events.callbacks.SongChangeEvent
import tech.thatgravyboat.jukebox.api.events.callbacks.UpdateEvent
import tech.thatgravyboat.jukebox.api.service.BaseService

object PlayerUIAddon : Addon {

    private val songUpdate: (UpdateEvent) -> Unit = { Player.updatePlayer(it.state) }
    private val songChange: (SongChangeEvent) -> Unit = { Player.changeSong() }

    override fun setup(service: BaseService) {
        service.registerListener(EventType.UPDATE, songUpdate)
        service.registerListener(EventType.SONG_CHANGE, songChange)
    }

    override fun close(service: BaseService) {
        service.unregisterListener(EventType.UPDATE, songUpdate)
        service.unregisterListener(EventType.SONG_CHANGE, songChange)
    }
}