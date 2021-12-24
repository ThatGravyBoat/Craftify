package tech.thatgravyboat.craftify.ui

import gg.essential.api.utils.GuiUtil
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.vigilance.gui.VigilancePalette
import net.minecraft.client.Minecraft
import tech.thatgravyboat.craftify.AlbumCache
import tech.thatgravyboat.craftify.Config
import tech.thatgravyboat.craftify.types.PlayerState
import tech.thatgravyboat.craftify.ui.enums.Position
import java.awt.Color
import java.net.URL

val background = Color(0, 0, 0, 80)

class UiPlayer : UIBlock(background) {

    init {
        constrain {
            height = 50.pixel()
            width = 150.pixel()
            y = 0.percent()
            x = 0.percent()
        }

        onMouseEnter {
            enableEffect(OutlineEffect(VigilancePalette.getAccent(), 1F, drawInsideChildren = true))
            if (Config.premiumControl) {
                setHeight(63.pixel())
                val pos = Position.values()[Config.position]
                if (pos.ordinal > 4) setY(pos.y(this) - 13.pixels())
                this.addChild(controls)
            }
        }

        onMouseLeave {
            removeEffect<OutlineEffect>()
            setHeight(50.pixel())
            val pos = Position.values()[Config.position]
            if (pos.ordinal > 4) setY(pos.y(this))
            this.removeChild(controls)
        }
    }

    private val image = UIBlock(VigilancePalette.getHighlight()).constrain {
        height = 40.pixel()
        width = 40.pixel()
        x = 5.pixel()
        y = 5.pixel()
    } childOf this

    private val info = UIContainer().constrain {
        width = 95.pixel()
        height = 40.pixel()
        x = 50.pixel()
        y = 5.pixel()
    } childOf this

    private val title = UITextMarquee(text = "Song Title").constrain {
        width = 100.percent()
        height = 10.pixel()
    } childOf info

    private val artist = UIWrappedText("Artists, here").constrain {
        width = 100.percent()
        height = 10.pixel()
        y = SiblingConstraint() + 2.pixel()
        textScale = 0.5.pixel()
    } childOf info

    private val progress = UIProgressBar().constrain {
        width = 100.percent()
        height = 3.pixel()
        y = 40.pixel() - 3.pixel()
    } childOf info

    private val controls = UIControls().constrain {
        width = 140.pixels()
        height = 10.pixels()
        y = 50.pixel()
        x = 5.pixels()
    }

    fun clientStop() {
        progress.tempStop()
    }

    fun updateState(state: PlayerState) {
        Minecraft.getMinecraft().addScheduledTask {
            progress.updateTime(state.getTime(), state.getEndTime())
            artist.setText(state.getArtists())
            title.updateText(state.getTitle())
            controls.updateState(state)

            try {
                image.clearChildren()
                image.addChild(
                    UIImage.ofURL(URL(state.getImage()), AlbumCache).constrain {
                        width = 100.percent()
                        height = 100.percent()
                    }
                )
            } catch (ignored: Exception) {
                // Ignoring exception due that it would be that Spotify sent a broken url.
            }
        }
    }

    override fun isHovered(): Boolean {
        return GuiUtil.getOpenedScreen() != null && super.isHovered()
    }
}
