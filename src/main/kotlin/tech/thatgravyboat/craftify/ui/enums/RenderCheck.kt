package tech.thatgravyboat.craftify.ui.enums

import net.minecraft.client.gui.GuiScreen

interface RenderCheck {
    fun canRender(gui: GuiScreen?): Boolean
}
