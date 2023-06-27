package tech.thatgravyboat.craftify.platform

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.Minecraft
import net.minecraftforge.fmlclient.registry.ClientRegistry

typealias MCKeyBinding = net.minecraft.client.KeyMapping
typealias MCEscMenu = net.minecraft.client.gui.screens.PauseScreen
typealias MCInventoryMenu = net.minecraft.client.gui.screens.inventory.InventoryScreen
typealias MCCreativeMenu = net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
typealias MCChatMenu = net.minecraft.client.gui.screens.ChatScreen

fun registerKeybinding(name: String, category: String, type: UKeybind.Type, code: Int): MCKeyBinding {
    val bind = MCKeyBinding(name,
        if (type == UKeybind.Type.MOUSE) InputConstants.Type.MOUSE else InputConstants.Type.KEYSYM,
        code,
        category
    )
    ClientRegistry.registerKeyBinding(bind)
    return bind
}

fun isPressed(bind: UKeybind): Boolean {
    return !bind.getBinding().isUnbound && bind.getBinding().isDown
}

fun runOnMcThread(block: () -> Unit) {
    Minecraft.getInstance().doRunTask(block)
}

fun isGuiHidden(): Boolean {
    return !Minecraft.renderNames()
}

fun isDebugGuiOpened(): Boolean {
    return Minecraft.getInstance().options.renderDebug
}

fun isTabOpened(): Boolean {
    val mc = Minecraft.getInstance()
    return mc.player?.let { player ->
        val obj = player.level.scoreboard.getDisplayObjective(0)
        return mc.options.keyPlayerList.isDown && (!mc.isLocalServer || player.connection.onlinePlayers.size > 1 || obj != null)
    } ?: false
}

fun registerCommand(name: String, commands: Map<String, Runnable>) {
    //DO NOTHING
}
