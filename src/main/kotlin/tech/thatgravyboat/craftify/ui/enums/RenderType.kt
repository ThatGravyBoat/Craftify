package tech.thatgravyboat.craftify.ui.enums

import net.minecraft.client.gui.screens.ChatScreen
import net.minecraft.client.gui.screens.PauseScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import tech.thatgravyboat.craftify.platform.*

enum class RenderType(val check: (Screen?) -> Boolean) {
    ESC_ONLY({ it is PauseScreen }),
    IN_GAME_ONLY({ it == null && !McClient.showDebugGui }),
    NON_INTRUSIVE({ if (it == null || it is PauseScreen) !McClient.showDebugGui else it is ChatScreen }),
    ALL({ it != null || !McClient.showDebugGui }),
    INV({ it is InventoryScreen }),
    ESC_INVENTORY({ it is PauseScreen || it is InventoryScreen || it is CreativeModeInventoryScreen }),
    TAB({ McClient.isTabOpened });

    fun canRender(gui: Screen?): Boolean = this.check(gui)

    override fun toString(): String = when (this) {
        ESC_ONLY -> "Esc Only"
        IN_GAME_ONLY -> "In-Game Only"
        NON_INTRUSIVE -> "Non Intrusive"
        ALL -> "Always"
        INV -> "In Inventory"
        ESC_INVENTORY -> "Esc/Inventory"
        TAB -> "Tab List"
    }

}
