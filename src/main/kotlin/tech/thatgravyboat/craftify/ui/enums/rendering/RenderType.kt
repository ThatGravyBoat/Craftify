package tech.thatgravyboat.craftify.ui.enums.rendering

import gg.essential.universal.utils.MCScreen
import tech.thatgravyboat.craftify.platform.MCChatMenu
import tech.thatgravyboat.craftify.platform.MCEscMenu
import tech.thatgravyboat.craftify.platform.MCInventoryMenu

enum class RenderType : RenderCheck {
    ESC_ONLY {
        override fun canRender(gui: MCScreen?): Boolean {
            return gui is MCEscMenu
        }
    },

    IN_GAME_ONLY {
        override fun canRender(gui: MCScreen?): Boolean {
            return gui == null
        }
    },

    NON_INTRUSIVE {
        override fun canRender(gui: MCScreen?): Boolean {
            return gui == null || gui is MCEscMenu || gui is MCChatMenu
        }
    },

    ALL {
        override fun canRender(gui: MCScreen?): Boolean {
            return true
        }
    },

    INV {
        override fun canRender(gui: MCScreen?): Boolean {
            return gui is MCInventoryMenu
        }
    };
}
