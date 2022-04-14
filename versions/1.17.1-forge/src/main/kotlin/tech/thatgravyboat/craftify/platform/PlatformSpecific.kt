package tech.thatgravyboat.craftify.platform

import gg.essential.universal.UMinecraft

typealias MCKeyBinding = net.minecraft.client.KeyMapping
typealias MCEscMenu = net.minecraft.client.gui.screens.PauseScreen
typealias MCInventoryMenu = net.minecraft.client.gui.screens.inventory.InventoryScreen
typealias MCChatMenu = net.minecraft.client.gui.screens.ChatScreen

fun registerKeybinding(name: String, category: String, type: UKeybind.Type, code: Int): MCKeyBinding {
    val bind = MCKeyBinding(name,
        if (type == UKeybind.Type.MOUSE)
            com.mojang.blaze3d.platform.InputConstants.Type.MOUSE
        else
            com.mojang.blaze3d.platform.InputConstants.Type.KEYSYM,
        code,
        category
    )
    net.minecraftforge.fmlclient.registry.ClientRegistry.registerKeyBinding(bind)
    return bind
}

fun isPressed(bind: UKeybind): Boolean {
    return !bind.getBinding().isUnbound && bind.getBinding().isDown
}

fun runOnMcThread(block: () -> Unit) {
    UMinecraft.getMinecraft().doRunTask(block)
}

fun isGuiHidden(): Boolean {
    return UMinecraft.getSettings().hideGui
}
