package tech.thatgravyboat.craftify.ui.enums.rendering

import gg.essential.universal.utils.MCScreen

interface RenderCheck {
    fun canRender(gui: MCScreen?): Boolean
}
