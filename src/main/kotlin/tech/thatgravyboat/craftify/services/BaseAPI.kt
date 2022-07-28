package tech.thatgravyboat.craftify.services

import gg.essential.api.EssentialAPI
import gg.essential.api.gui.Slot
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import tech.thatgravyboat.craftify.types.PlayerState
import java.net.URL

interface BaseAPI {

    fun getState(): PlayerState?

    fun restartPoller()

    fun startPoller()

    fun stopPoller(): Boolean

    fun changePlayingState(playing: Boolean)

    fun toggleShuffle(shuffling: Boolean)

    fun toggleRepeat(repeating: Boolean)

    fun skip(forward: Boolean)

    fun setVolume(volume: Int, showNotification: Boolean = false)

    fun showVolumeNotification(volume: Int) {
        val image = when {
            volume <= 0 -> "https://i.imgur.com/v2a3Z8n.png"
            volume <= 30 -> "https://i.imgur.com/8L4av1O.png"
            volume <= 70 -> "https://i.imgur.com/tGJKxRr.png"
            else -> "https://i.imgur.com/1Ay43hi.png"
        }
        EssentialAPI.getNotifications().push(
            title = "Craftify",
            message = "The volume has been set to $volume%",
            configure = {
                this.withCustomComponent(
                    Slot.PREVIEW,
                    UIImage.ofURL(URL(image)).constrain {
                        width = 25.pixels()
                        height = 25.pixels()
                    }
                )
            }
        )
    }

    fun openTrack()
}