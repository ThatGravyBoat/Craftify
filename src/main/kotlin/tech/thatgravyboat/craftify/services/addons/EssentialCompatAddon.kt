package tech.thatgravyboat.craftify.services.addons

import tech.thatgravyboat.craftify.utils.EssentialUtils
import tech.thatgravyboat.jukebox.api.events.EventType
import tech.thatgravyboat.jukebox.api.events.callbacks.VolumeChangeEvent
import tech.thatgravyboat.jukebox.api.service.BaseService

object EssentialCompatAddon : Addon {

    private val handler: (VolumeChangeEvent) -> Unit = {
        if (it.shouldNotify) {
            val image = when {
                it.volume <= 0 -> "https://images.teamresourceful.com/u/cwD8Hn.png"
                it.volume <= 30 -> "https://images.teamresourceful.com/u/xtBaQY.png"
                it.volume <= 70 -> "https://images.teamresourceful.com/u/DWDtyo.png"
                else -> "https://images.teamresourceful.com/u/FKSB9b.png"
            }
            EssentialUtils.sendNotification(
                title = "Craftify",
                message = "The volume has been set to ${it.volume}%",
                image = image,
            )
        }
    }

    override fun setup(service: BaseService) {
        service.registerListener(EventType.VOLUME_CHANGE, handler)
    }

    override fun close(service: BaseService) {
        service.unregisterListener(EventType.VOLUME_CHANGE, handler)
    }
}