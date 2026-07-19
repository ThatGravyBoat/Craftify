package tech.thatgravyboat.craftify.utils

import com.mojang.blaze3d.platform.NativeImage
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers
import earth.terrarium.olympus.client.pipelines.RoundedRectangle
import net.minecraft.client.gui.GuiGraphicsExtractor
import earth.terrarium.olympus.client.utils.State as OlympusState
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.network.chat.Component
import net.minecraft.util.ARGB
import tech.thatgravyboat.craftify.platform.pushPop
import tech.thatgravyboat.craftify.platform.scale
import tech.thatgravyboat.craftify.platform.translate
import tech.thatgravyboat.craftify.ui.v2.MarqueeWidgetRenderer
import java.awt.Color
import java.awt.image.BufferedImage
import com.teamresourceful.resourcefullib.common.color.Color as OlympusColor

fun Color.toOlympus(): OlympusColor = OlympusColor(red, green, blue, alpha)

fun BufferedImage.toNative(): NativeImage {
    val native = NativeImage(NativeImage.Format.RGBA, this.width, this.height, false)

    for (x in 0 until this.width) {
        for (y in 0 until this.height) {
            native.setPixelABGR(x, y, ARGB.toABGR(this.getRGB(x, y)))
        }
    }

    return native
}

fun <T : AbstractWidget> WidgetRenderer<T>.withScale(scale: Float): WidgetRenderer<T> {
    return WidgetRenderer { graphics, ctx, partialTicks ->
        if (scale == 1f) {
            this.render(graphics, ctx, partialTicks)
        } else {
            val newCtx = ctx.copy()
                .setWidth((ctx.width / scale).toInt())
                .setHeight((ctx.height / scale).toInt())
                .setX(0)
                .setY(0)

            graphics.pushPop {
                graphics.translate(ctx.x.toFloat(), ctx.y.toFloat())
                graphics.scale(scale, scale)
                this.render(graphics, newCtx, partialTicks)
            }
        }
    }
}

fun <T : AbstractWidget> OlympusState<String>.asRenderer(alignment: Float = 0f, color: Color, scale: Float = 1f): WidgetRenderer<T> {
    val color = color.toOlympus()
    return WidgetRenderer<T> { graphics, context, partialTicks ->
        WidgetRenderers.text<T>(Component.literal(this.get()))
            .withShadow()
            .withAlignment(alignment)
            .withColor(color)
            .withScale(scale)
            .render(graphics, context, partialTicks)
    }
}

fun <T : AbstractWidget> String.asRenderer(alignment: Float = 0f, color: Color, scale: Float = 1f): WidgetRenderer<T> {
    val color = color.toOlympus()
    return MarqueeWidgetRenderer<T>(Component.literal(this))
        .withShadow()
        .withAlignment(alignment)
        .withColor(color)
        .withScale(scale)
}

fun GuiGraphicsExtractor.fillRounded(x: Int, y: Int, width: Int, height: Int, color: Color, radius: Float) {
    RoundedRectangle.draw(this, x, y, width, height, color.rgb, 0, radius, 0)
}

fun <T> OlympusState(getter: () -> T): OlympusState<T> {
    return object : OlympusState<T> {
        override fun set(value: T) {}
        override fun get(): T = getter()
    }
}

fun <T, R> OlympusState<T>.map(mapper: (T) -> R): OlympusState<R> {
    val state = this
    return object : OlympusState<R> {
        override fun set(value: R) {}
        override fun get(): R = mapper(state.get())
    }
}

fun <T> OlympusState<T?>.isEmpty(): Boolean {
    return this.get() == null
}

fun OlympusState<Boolean?>.toggle() {
    this.set(this.get() != true)
}