package tech.thatgravyboat.craftify.platform

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

expect inline fun GuiGraphics.pushPop(runnable: () -> Unit)
expect fun GuiGraphics.scale(x: Number, y: Number)
expect fun GuiGraphics.translate(x: Number, y: Number)
expect fun GuiGraphics.drawTexture(
    texture: ResourceLocation, x: Int, y: Int, width: Int, height: Int,
    u0: Float = 0f, v0: Float = 0f, u1: Float = 1f, v1: Float = 1f,
    color: Int = -1
)
expect fun GuiGraphics.drawSprite(
    texture: ResourceLocation, x: Int, y: Int, width: Int, height: Int,
)
expect fun GuiGraphics.drawString(
    text: Component, x: Int, y: Int, color: Int = -1, dropShadow: Boolean = false
)