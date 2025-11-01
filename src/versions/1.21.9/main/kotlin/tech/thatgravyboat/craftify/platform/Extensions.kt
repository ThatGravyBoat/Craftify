package tech.thatgravyboat.craftify.platform

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.gui.render.state.BlitRenderState
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.ResourceLocation
import org.joml.Matrix3x2f

actual inline fun GuiGraphics.pushPop(runnable: () -> Unit) {
    this.pose().pushMatrix()
    runnable()
    this.pose().popMatrix()
}

actual fun GuiGraphics.scale(x: Number, y: Number) {
    this.pose().scale(x.toFloat(), y.toFloat())
}

actual fun GuiGraphics.translate(x: Number, y: Number) {
    this.pose().translate(x.toFloat(), y.toFloat())
}


actual fun GuiGraphics.drawTexture(texture: ResourceLocation, x: Int, y: Int, width: Int, height: Int, u0: Float, v0: Float, u1: Float, v1: Float, color: Int) {
    val minx = x
    val miny = y
    val maxx = (x + width)
    val maxy = (y + height)

    this.guiRenderState.submitGuiElement(
        BlitRenderState(
            RenderPipelines.GUI_TEXTURED, TextureSetup.singleTexture(McClient.self.textureManager.getTexture(texture).textureView), Matrix3x2f(this.pose()),
            minx, miny, maxx, maxy, u0, u1, v0, v1, color,
            this.scissorStack.peek(),
        ),
    )
}

actual fun GuiGraphics.drawSprite(
    texture: ResourceLocation, x: Int, y: Int, width: Int, height: Int,
) {
    this.blitSprite(RenderPipelines.GUI_TEXTURED, texture, x, y, width, height)
}