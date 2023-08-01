package tech.thatgravyboat.craftify.ui

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.universal.UScreen
import gg.essential.vigilance.gui.VigilancePalette
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.platform.runOnMcThread
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.ui.constraints.ConfigColorConstraint
import tech.thatgravyboat.craftify.ui.enums.Anchor
import tech.thatgravyboat.craftify.utils.SingleImageCache
import tech.thatgravyboat.craftify.utils.Utils.clearFormatting
import tech.thatgravyboat.jukebox.api.state.State
import java.net.URL
import java.util.concurrent.CompletableFuture
import kotlin.math.min

class UIPlayer : UIBlock(ConfigColorConstraint("background")) {

    init {
        constrain {
            height = 50.pixel()
            width = 150.pixel()
            y = 0.percent()
            x = 0.percent()
        }

        onMouseEnter {
            enableEffect(OutlineEffect(ThemeConfig.borderColor, 1F, drawInsideChildren = true))
            if (Config.premiumControl) {
                setHeight(63.pixel())
                val pos = Anchor.values()[Config.anchorPoint]
                if (pos.ordinal > 4) setY(pos.getY(this) - 13.pixels())
                this.addChild(controls)
            }
        }

        onMouseLeave {
            removeEffect<OutlineEffect>()
            setHeight(50.pixel())
            val pos = Anchor.values()[Config.anchorPoint]
            if (pos.ordinal > 4) setY(pos.getY(this))
            this.removeChild(controls)
        }
    }

    private var imageUrl = ""

    private val image by UIBlock(VigilancePalette.getHighlight()).constrain {
        height = 40.pixel()
        width = 40.pixel()
        x = 5.pixel()
        y = 5.pixel()
    } childOf this

    private val info by UIContainer().constrain {
        width = 95.pixel()
        height = 40.pixel()
        x = 50.pixel()
        y = 5.pixel()
    } childOf this

    private val title by UITextMarquee(text = "Song Title").constrain {
        width = 100.percent()
        height = 10.pixel()
        color = ConfigColorConstraint("title")
    } childOf info

    private val artist by lazy { UIWrappedText("Artists, here").constrain {
        width = 100.percent()
        height = 10.pixel()
        y = 12.pixel()
        textScale = 0.5.pixel()
        color = ConfigColorConstraint("artist")
    } childOf info }

    private val progress by UIProgressBar().constrain {
        width = 100.percent()
        height = 3.pixel()
        y = 40.pixel() - 3.pixel()
    } childOf info

    private val controls by UIControls().constrain {
        width = ChildBasedSizeConstraint()
        height = 10.pixels()
        y = 50.pixel()
        x = CenterConstraint()
    }

    fun clientStop() {
        progress.tempStop()
    }

    fun updateState(state: State) {
        runOnMcThread {
            progress.updateTime(state.songState.progress, state.songState.duration)
            val artistText = state.song.artists.subList(0, state.song.artists.size.coerceAtMost(3)).joinToString(", ")
            artist.setText(artistText.clearFormatting())
            title.updateText(state.song.title)
            controls.updateState(state)

            try {
                if (imageUrl != state.song.cover && state.song.cover.isNotEmpty()) {
                    imageUrl = state.song.cover
                    synchronized(image.children) {
                        val url = URL(state.song.cover)
                        image.clearChildren()
                        image.addChild(
                            UIImage(CompletableFuture.supplyAsync {
                                return@supplyAsync SingleImageCache[url] ?: UIImage.get(url).let {
                                    val scale = min(it.width / 40, it.height / 40);
                                    it.getSubimage(it.width / 2 - 20 * scale, it.height / 2 - 20 * scale, 40 * scale, 40 * scale);
                                }.also {
                                    SingleImageCache[url] = it
                                }
                            }, EmptyImageProvider, EmptyImageProvider).constrain {
                                width = 100.percent()
                                height = 100.percent()
                            }
                        )
                    }
                }
            } catch (ignored: Exception) {
                // Ignoring exception due that it would be that Spotify sent a broken url.
            }
        }
    }

    fun updateTheme() {
        progress.updateTheme()
        controls.updateTheme()
        if (ThemeConfig.hideImage) {
            this.removeChild(image)
            info.setX(5.pixel())
            this.setWidth(105.pixel())
        } else {
            this.addChild(image)
            info.setX(50.pixel())
            this.setWidth(150.pixel())
        }
    }

    override fun isHovered(): Boolean {
        return UScreen.currentScreen != null && super.isHovered()
    }
}
