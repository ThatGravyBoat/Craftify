package tech.thatgravyboat.craftify.platform

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.gui.render.state.BlitRenderState
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import org.joml.Matrix3x2f

inline fun GuiGraphics.pushPop(runnable: () -> Unit) {
    this.pose().pushMatrix()
    runnable()
    this.pose().popMatrix()
}

fun GuiGraphics.scale(x: Number, y: Number) {
    this.pose().scale(x.toFloat(), y.toFloat())
}

fun GuiGraphics.translate(x: Number, y: Number) {
    this.pose().translate(x.toFloat(), y.toFloat())
}

fun GuiGraphics.drawTexture(
    texture: Identifier, x: Int, y: Int, width: Int, height: Int,
    u0: Float = 0f, v0: Float = 0f, u1: Float = 1f, v1: Float = 1f,
    color: Int = -1
) {
    val minx = x
    val miny = y
    val maxx = (x + width)
    val maxy = (y + height)

    val texture = McClient.self.textureManager.getTexture(texture)

    this.guiRenderState.submitGuiElement(
        BlitRenderState(
            RenderPipelines.GUI_TEXTURED, TextureSetup.singleTexture(texture.textureView, texture.sampler), Matrix3x2f(this.pose()),
            minx, miny, maxx, maxy, u0, u1, v0, v1, color,
            this.scissorStack.peek(),
        ),
    )
}

fun GuiGraphics.drawSprite(
    texture: Identifier, x: Int, y: Int, width: Int, height: Int,
) {
    this.blitSprite(RenderPipelines.GUI_TEXTURED, texture, x, y, width, height)
}

fun GuiGraphics.drawString(
    text: Component, x: Int, y: Int, color: Int = -1, dropShadow: Boolean = false
) {
    this.drawString(McClient.font, text, x, y, color, dropShadow)
}