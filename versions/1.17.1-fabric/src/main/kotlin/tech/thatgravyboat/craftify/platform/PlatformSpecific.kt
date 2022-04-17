package tech.thatgravyboat.craftify.platform

import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil

typealias MCKeyBinding = net.minecraft.client.option.KeyBinding
typealias MCEscMenu = net.minecraft.client.gui.screen.GameMenuScreen
typealias MCInventoryMenu = net.minecraft.client.gui.screen.ingame.InventoryScreen
typealias MCChatMenu = net.minecraft.client.gui.screen.ChatScreen

fun registerKeybinding(name: String, category: String, type: UKeybind.Type, code: Int): MCKeyBinding {
    val bind = MCKeyBinding(name,
        if (type == UKeybind.Type.MOUSE) InputUtil.Type.MOUSE else InputUtil.Type.KEYSYM,
        code,
        category
    )
    net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding(bind)
    return bind
}

fun isPressed(bind: UKeybind): Boolean {
    return !bind.getBinding().isUnbound && bind.getBinding().wasPressed()
}

fun runOnMcThread(block: () -> Unit) {
    MinecraftClient.getInstance().executeTask(block)
}

fun isGuiHidden(): Boolean {
    return !MinecraftClient.isHudEnabled()
}
