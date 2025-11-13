package tech.thatgravyboat.craftify.platform

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderType
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

actual inline fun GuiGraphics.pushPop(runnable: () -> Unit) {
    this.pose().pushPose()
    runnable()
    this.pose().popPose()
}

actual fun GuiGraphics.scale(x: Number, y: Number) {
    this.pose().scale(x.toFloat(), y.toFloat(), 1f)
}

actual fun GuiGraphics.translate(x: Number, y: Number) {
    this.pose().translate(x.toDouble(), y.toDouble(), 0.0)
}


actual fun GuiGraphics.drawTexture(texture: ResourceLocation, x: Int, y: Int, width: Int, height: Int, u0: Float, v0: Float, u1: Float, v1: Float, color: Int) {
    val matrix = this.pose().last().pose()
    val minx = x.toFloat()
    val miny = y.toFloat()
    val maxx = (x + width).toFloat()
    val maxy = (y + height).toFloat()


    this.drawSpecial { source ->
        val buffer = source.getBuffer(RenderType.guiTextured(texture))
        buffer.addVertex(matrix, minx, miny, 0f).setColor(color).setUv(u0, v0)
        buffer.addVertex(matrix, minx, maxy, 0f).setColor(color).setUv(u0, v1)
        buffer.addVertex(matrix, maxx, maxy, 0f).setColor(color).setUv(u1, v1)
        buffer.addVertex(matrix, maxx, miny, 0f).setColor(color).setUv(u1, v0)
    }
}

actual fun GuiGraphics.drawSprite(
    texture: ResourceLocation, x: Int, y: Int, width: Int, height: Int,
) {
    this.blitSprite(RenderType::guiTextured, texture, x, y, width, height)
}
actual fun GuiGraphics.drawString(text: Component, x: Int, y: Int, color: Int, dropShadow: Boolean) {
    this.drawString(McClient.font, text, x, y, color, dropShadow)
}