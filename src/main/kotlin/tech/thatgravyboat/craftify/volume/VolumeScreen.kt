package tech.thatgravyboat.craftify.volume

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.*
import tech.thatgravyboat.craftify.api.SpotifyAPI
import java.awt.Color
import java.net.URL

class VolumeScreen : WindowScreen(version = ElementaVersion.V1, restoreCurrentGuiOnClose = true) {

    private var volume: Float = getVolume()
    private var lastVolume: Float = getVolume()

    private fun getVolume(): Float {
        return SpotifyAPI.lastStateData?.getAsJsonObject("device")?.get("volume_percent")?.asInt?.toFloat()?.div(100f) ?: 0.25f
    }

    private val container by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        height = 30.pixel()
        width = 140.pixel()
    } childOf window

    private val box by UIBlock(Color(0, 0, 0, 80)).constrain {
        height = 100.percent()
        width = 100.percent()
    } childOf container

    private val text by UIText("Set Volume").constrain {
        x = CenterConstraint()
        y = 5.pixels()
    } childOf container

    private var icon: UIImage? = null

    init {
        createVolumeButton()
    }

    private val slide by VolumeSlider(volume).constrain {
        x = 22.pixels()
        y = 18.pixels()
        width = 90.pixels()
        height = 7.pixels()
    } childOf box

    private val percentage by UIText("${(volume*100f).toInt()}%").constrain {
        x = 117.pixels()
        y = 19.pixels()
        textScale = 0.5.pixel()
    } childOf box

    init {
        slide.onValueChange {
            if (volume == 0f || it == 0f) {
                volume = it
                createVolumeButton()
            }else {
                volume = it
            }
            percentage.setText("${(volume*100f).toInt()}%")
        }

        slide.onValueChanged {
            SpotifyAPI.setVolume((it*100f).toInt())
            volume = it
        }
    }

    private fun createVolumeButton() {
        icon?.let {
            box.removeChild(it)
        }

        icon = UIImage.ofURL(URL(if (volume > 0f) "https://i.imgur.com/1Ay43hi.png" else "https://i.imgur.com/v2a3Z8n.png")).constrain {
            x = 5.pixels()
            y = 15.pixels()
            width = 12.pixels()
            height = 12.pixels()
        } childOf box

        icon?.onMouseClick {
            if (volume > 0f) {
                lastVolume = volume
                volume = 0f
            } else {
                volume = lastVolume
            }
            slide.setCurrentPercentage(volume)
            SpotifyAPI.setVolume((volume*100f).toInt())
            createVolumeButton()
        }
        icon?.onMouseEnter {
            this.setColor(Color.GRAY)
        }
        icon?.onMouseLeave {
            this.setColor(Color.WHITE)
        }
    }
}
