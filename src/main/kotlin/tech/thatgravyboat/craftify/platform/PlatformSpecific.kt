package tech.thatgravyboat.craftify.platform

import gg.essential.universal.UKeyboard
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.client.registry.ClientRegistry

typealias MCKeyBinding = net.minecraft.client.settings.KeyBinding
typealias MCEscMenu = net.minecraft.client.gui.GuiIngameMenu
typealias MCInventoryMenu = net.minecraft.client.gui.inventory.GuiInventory
typealias MCChatMenu = net.minecraft.client.gui.GuiChat

fun registerKeybinding(name: String, category: String, type: UKeybind.Type, code: Int): MCKeyBinding {
    val bind = MCKeyBinding(name, if (type == UKeybind.Type.MOUSE) code - 100 else code, category)
    ClientRegistry.registerKeyBinding(bind)
    return bind
}

fun isPressed(bind: UKeybind): Boolean {
    return bind.getBinding().keyCode != UKeyboard.KEY_NONE && bind.getBinding().isPressed
}

fun runOnMcThread(block: () -> Unit) {
    Minecraft.getMinecraft().addScheduledTask(block)
}

fun isGuiHidden(): Boolean {
    return !Minecraft.isGuiEnabled()
}
