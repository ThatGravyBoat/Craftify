package tech.thatgravyboat.craftify.ui

import earth.terrarium.olympus.client.components.Widgets
import earth.terrarium.olympus.client.layouts.Layouts
import earth.terrarium.olympus.client.layouts.LinearViewLayout
import earth.terrarium.olympus.client.pipelines.RoundedRectangle
import earth.terrarium.olympus.client.pipelines.RoundedTexture
import earth.terrarium.olympus.client.utils.Orientation
import net.minecraft.client.gui.GuiGraphics
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.platform.mouseClicked
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.ui.v2.BaseRenderable
import tech.thatgravyboat.craftify.ui.v2.UIPlayerControls
import tech.thatgravyboat.craftify.utils.*
import tech.thatgravyboat.jukebox.api.state.State
import java.net.URI

private const val PADDING = 5
private const val WIDTH = 150
private const val HEIGHT = 50
private const val FOCUSED_HEIGHT = 63

object UIPlayerV2 : BaseRenderable(WIDTH, HEIGHT) {

    override val x: Int get() = Config.anchorPoint.getX(this.width, Config.xOffset)
    override val y: Int get() = Config.anchorPoint.getY(this.height, Config.yOffset)

    private var layout: LinearViewLayout? = null
    private var hoveredLayout: LinearViewLayout? = null

    override fun onHoverChange(mouseX: Int, mouseY: Int) {
        this.height = if (this.isHovered && Config.premiumControl) FOCUSED_HEIGHT else HEIGHT
    }

    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTicks: Float) {
        val layout = this.layout ?: return
        val hoveredLayout = this.hoveredLayout ?: return

        RoundedRectangle.draw(
            graphics,
            x, y, width, height,
            ThemeConfig.backgroundColor.rgb, if (this.isHovered) ThemeConfig.borderColor.rgb else ThemeConfig.backgroundColor.rgb,
            ThemeConfig.backgroundRadius, 2
        )

        layout.setPosition(x + PADDING, y + PADDING)
        layout.build { it.render(graphics, mouseX, mouseY, partialTicks) }

        if (this.isHovered && Config.premiumControl) {
            hoveredLayout.arrangeElements()
            hoveredLayout.setPosition(x + PADDING + (WIDTH - PADDING * 2 - hoveredLayout.width) / 2, y + PADDING + 40 + 4)
            hoveredLayout.build { it.render(graphics, mouseX, mouseY, partialTicks) }
        }
    }

    fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (this.isHovered) {
            val hoveredLayout = this.hoveredLayout ?: return false
            var clicked = false
            hoveredLayout.visitWidgets {
                if (!clicked && it.isHovered && it.mouseClicked(mouseX, mouseY, button)) {
                    clicked = true
                }
            }

            return clicked
        }
        return false
    }

    fun setState(state: State) {
        val artists = state.song.artists.take(2).joinToString(", ") + if (state.song.artists.size > 3) ", ..." else ""

        val startTime = System.currentTimeMillis()
        val uri = runCatching { URI.create(state.song.cover) }.getOrNull()

        val progress = OlympusState {
            val start = state.songState.progress * 1000f
            val end = state.songState.duration * 1000f
            val offset = if (state.songState.isPlaying) System.currentTimeMillis() - startTime else 0

            (start + offset) to end
        }

        val image = Widgets.renderable { graphics, ctx, _ ->
            val texture = uri?.let(ImageCaches.ALBUM::get)
            if (texture != null && texture != ImageCaches.MISSING_TEXTURE) {
                RoundedTexture.draw(graphics, ctx.x, ctx.y, ctx.width, ctx.height, texture, 0f, 0f, 1f, 1f, ThemeConfig.backgroundRadius)
            } else {
                RoundedRectangle.draw(graphics, ctx.x, ctx.y, ctx.width, ctx.height, ThemeConfig.backgroundColor.rgb, 0, ThemeConfig.backgroundRadius, 0)
            }
        }

        val textContainer = Widgets.frame { frame ->
            frame.withSize(95, 18)
            frame.withEqualSpacing(Orientation.VERTICAL)
            frame.withContents { layout ->
                layout.addChild(Widgets.renderable(state.song.title.asRenderer(0f, ThemeConfig.titleColor, 1f)).withSize(95, 10))
                layout.addChild(Widgets.renderable(artists.asRenderer(0f, ThemeConfig.artistColor, 0.5f)).withSize(95, 6))
            }
        }

        val progressContainer = Widgets.frame { frame ->
            frame.withSize(95, 11)
            frame.withEqualSpacing(Orientation.VERTICAL)
            frame.withContents { layout ->
                layout.addChild(Widgets.frame { frame ->
                    frame.withSize(95, 7)
                    frame.withEqualSpacing(Orientation.HORIZONTAL)
                    frame.withContents { layout ->
                        fun formatTime(ms: Float) = String.format("%02d:%02d", ((ms / 1000) / 60).toInt(), ((ms / 1000) % 60).toInt())

                        val start = progress.map { (time, _) -> formatTime(time) }
                        val end = progress.map { (_, total) -> formatTime(total) }

                        layout.addChild(Widgets.renderable(start.asRenderer(0f, ThemeConfig.progressNumberColor, 0.5f)).withSize(30, 7))
                        layout.addChild(Widgets.renderable(end.asRenderer(1f, ThemeConfig.progressNumberColor, 0.5f)).withSize(30, 7))
                    }
                })
                layout.addChild(
                    Widgets.renderable({ graphics, ctx, _ ->
                        val (time, total) = progress.get()
                        val percent = (time / total).coerceIn(0f, 1f)

                        graphics.fillRounded(ctx.x, ctx.y, ctx.width, ctx.height, ThemeConfig.progressBackgroundColor, ThemeConfig.progressRadius)
                        graphics.fillRounded(ctx.x, ctx.y, (ctx.width * percent).toInt(), ctx.height, ThemeConfig.progressColor, ThemeConfig.progressRadius)
                    }) {
                        it.withSize(95, 3)
                    }
                )
            }
        }

        this.layout = Layouts.row()
            .withGap(PADDING)
            .withChild(image.withSize(40, 40))
            .withChild(Widgets.frame { frame ->
                frame.withSize(95, 40)
                frame.withEqualSpacing(Orientation.VERTICAL)
                frame.withContents { layout ->
                    layout.addChild(textContainer)
                    layout.addChild(progressContainer)
                }
            })

        this.hoveredLayout = UIPlayerControls.create(state)
    }
}