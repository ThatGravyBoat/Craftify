package tech.thatgravyboat.craftify.ui.v2

import com.mojang.blaze3d.platform.cursor.CursorTypes
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext
import earth.terrarium.olympus.client.components.buttons.Button
import earth.terrarium.olympus.client.utils.State
import net.minecraft.client.gui.GuiGraphicsExtractor
import tech.thatgravyboat.craftify.platform.drawTexture
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.utils.ImageCaches
import java.net.URI

class ControlButtonWidgetRenderer(
    url: String,
    selected: String,
    private val state: State<Boolean?>
) : WidgetRenderer<Button> {

    private val canUseSelectedColor = url == selected
    private val texture = runCatching { URI.create(url) }.getOrNull()
    private val selectedTexture = runCatching { URI.create(selected) }.getOrNull()

    override fun render(graphics: GuiGraphicsExtractor, ctx: WidgetRendererContext<Button>, partialTicks: Float) {
        val state = this.state.get() == true
        val texture = ImageCaches.ASSET.get(if (state) selectedTexture else this.texture)
        if (texture != null && texture != ImageCaches.MISSING_TEXTURE) {
            val color = when {
                ctx.widget.isHovered && state && canUseSelectedColor -> ThemeConfig.selectedHoverControlColor
                state && canUseSelectedColor -> ThemeConfig.selectedControlColor
                ctx.widget.isHovered -> ThemeConfig.hoverControlColor
                else -> ThemeConfig.controlColor
            }

            graphics.drawTexture(
                texture = texture,
                x = ctx.x, y = ctx.y,
                width = ctx.width, height = ctx.height,
                color = color.rgb
            )
        }

        if (ctx.widget.isHovered) {
            graphics.requestCursor(CursorTypes.POINTING_HAND)
        }
    }
}