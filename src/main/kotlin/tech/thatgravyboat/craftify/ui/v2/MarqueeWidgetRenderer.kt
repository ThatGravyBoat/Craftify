package tech.thatgravyboat.craftify.ui.v2

import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext
import earth.terrarium.olympus.client.components.renderers.ColorableWidget
import earth.terrarium.olympus.client.constants.MinecraftColors
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.platform.drawString
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import com.teamresourceful.resourcefullib.common.color.Color as OlympusColor

private const val SCROLL_GAP = 20

class MarqueeWidgetRenderer<T : AbstractWidget>(private val text: Component) : WidgetRenderer<T>, ColorableWidget {

    private var font: Font = Minecraft.getInstance().font
    private var color: OlympusColor = MinecraftColors.DARK_GRAY
    private var drawShadow = false

    private var alignX = 0.5f

    override fun render(graphics: GuiGraphicsExtractor, context: WidgetRendererContext<T?>, partialTick: Float) {
        val textWidth = this.font.width(this.text)
        val centerY = context.y + context.height / 2 - font.lineHeight / 2
        val color = if (this.color.intAlpha == 0) this.color.value or 0xFF000000.toInt() else this.color.value

        if (textWidth > context.width) {
            val gap = min(SCROLL_GAP, context.width)
            val speedModifier = max(6 - Config.marqueeSpeed, 1)
            val ticks = Minecraft.getInstance().gui.guiTicks.let { Mth.lerp(partialTick, it - 1f, it.toFloat()) } / speedModifier
            val offset = (ticks % (textWidth + gap)).roundToInt()

            graphics.enableScissor(context.left, context.top, context.right, context.bottom)
            graphics.drawString(this.text, context.x - offset, centerY, color, this.drawShadow)
            graphics.drawString(this.text, context.x - offset + textWidth + gap, centerY, color, this.drawShadow)
            graphics.disableScissor()
        } else {
            val x = context.x + (context.width * alignX).roundToInt()
            graphics.drawString(this.text, x - (textWidth * alignX).roundToInt(), centerY, color, this.drawShadow)
        }
    }

    override fun withShadow(): MarqueeWidgetRenderer<T> {
        this.drawShadow = true
        return this
    }

    override fun withColor(color: OlympusColor): MarqueeWidgetRenderer<T> {
        this.color = color
        return this
    }

    fun withFont(font: Font): MarqueeWidgetRenderer<T> {
        this.font = font
        return this
    }

    fun withAlignment(alignX: Float): MarqueeWidgetRenderer<T> {
        this.alignX = alignX
        return this
    }

    fun withLeftAlignment(): MarqueeWidgetRenderer<T> {
        this.alignX = 0f
        return this
    }

    fun withCenterAlignment(): MarqueeWidgetRenderer<T> {
        this.alignX = 0.5f
        return this
    }

    fun withRightAlignment(): MarqueeWidgetRenderer<T> {
        this.alignX = 1f
        return this
    }
}