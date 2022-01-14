package tech.thatgravyboat.craftify.ui.enums.rendering

import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.GuiIngameMenu
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiInventory

enum class RenderType : RenderCheck {
    ESC_ONLY {
        override fun canRender(gui: GuiScreen?): Boolean {
            return gui is GuiIngameMenu
        }
    },

    IN_GAME_ONLY {
        override fun canRender(gui: GuiScreen?): Boolean {
            return gui == null
        }
    },

    NON_INTRUSIVE {
        override fun canRender(gui: GuiScreen?): Boolean {
            return gui == null || gui is GuiIngameMenu || gui is GuiChat
        }
    },

    ALL {
        override fun canRender(gui: GuiScreen?): Boolean {
            return true
        }
    },

    INV {
        override fun canRender(gui: GuiScreen?): Boolean {
            return gui is GuiInventory
        }
    };
}
