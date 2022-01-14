package tech.thatgravyboat.craftify.ui.enums.rendering

import net.minecraft.client.gui.GuiScreen

interface RenderCheck {
    fun canRender(gui: GuiScreen?): Boolean
}
