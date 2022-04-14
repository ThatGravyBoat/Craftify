package tech.thatgravyboat.craftify.platform

import gg.essential.universal.UKeyboard
import gg.essential.universal.UMinecraft

typealias MCKeyBinding = net.minecraft.client.settings.KeyBinding
typealias MCEscMenu = net.minecraft.client.gui.GuiIngameMenu
typealias MCInventoryMenu = net.minecraft.client.gui.inventory.GuiInventory
typealias MCChatMenu = net.minecraft.client.gui.GuiChat

fun registerKeybinding(name: String, category: String, type: UKeybind.Type, code: Int): MCKeyBinding {
    val bind = MCKeyBinding(name, if (type == UKeybind.Type.MOUSE) code - 100 else code, category)
    net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(bind)
    return bind
}

fun isPressed(bind: UKeybind): Boolean {
    return bind.getBinding().keyCode != UKeyboard.KEY_NONE && bind.getBinding().isPressed
}

fun runOnMcThread(block: () -> Unit) {
    UMinecraft.getMinecraft().addScheduledTask(block)
}

fun isGuiHidden(): Boolean {
    return UMinecraft.getSettings().hideGUI
}
