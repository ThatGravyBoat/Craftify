package tech.thatgravyboat.craftify.ui.v2

import net.minecraft.client.gui.GuiGraphicsExtractor

abstract class BaseRenderable(var width: Int, var height: Int) {

    abstract val x: Int
    abstract val y: Int

    protected var isHovered = false
        private set

    fun renderWithEffects(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTicks: Float) {
        val hovered = mouseX in x until (x + width) && mouseY in y until (y + height)
        if (hovered != this.isHovered) {
            this.isHovered = hovered
            onHoverChange(mouseX, mouseY)
        }

        this.render(graphics, mouseX, mouseY, partialTicks)
    }

    open fun onHoverChange(mouseX: Int, mouseY: Int) {
    }

    abstract fun render(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTicks: Float)
}