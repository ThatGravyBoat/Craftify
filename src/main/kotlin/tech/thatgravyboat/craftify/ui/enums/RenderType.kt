package tech.thatgravyboat.craftify.ui.enums

import gg.essential.universal.utils.MCScreen
import tech.thatgravyboat.craftify.platform.*

enum class RenderType {
    ESC_ONLY {
        override fun canRender(gui: MCScreen?): Boolean {
            return gui is MCEscMenu
        }
    },

    IN_GAME_ONLY {
        override fun canRender(gui: MCScreen?): Boolean {
            return gui == null && !isDebugGuiOpened()
        }
    },

    NON_INTRUSIVE {
        override fun canRender(gui: MCScreen?): Boolean {
            if (gui == null || gui is MCEscMenu) {
                return !isDebugGuiOpened()
            }
            return gui is MCChatMenu
        }
    },

    ALL {
        override fun canRender(gui: MCScreen?): Boolean {
            return gui != null || !isDebugGuiOpened()
        }
    },

    INV {
        override fun canRender(gui: MCScreen?): Boolean {
            return gui is MCInventoryMenu
        }
    },

    ESC_INVENTORY {
        override fun canRender(gui: MCScreen?): Boolean {
            return gui is MCEscMenu || gui is MCInventoryMenu || gui is MCCreativeMenu
        }
    },

    TAB {
        override fun canRender(gui: MCScreen?): Boolean {
            return isTabOpened()
        }
    };

    open fun canRender(gui: MCScreen?): Boolean {
        throw NotImplementedError()
    }
}
