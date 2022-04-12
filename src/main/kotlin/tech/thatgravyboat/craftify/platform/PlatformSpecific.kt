package tech.thatgravyboat.craftify.platform

import gg.essential.universal.UKeyboard
import gg.essential.universal.UMinecraft

typealias MCKeyBinding =
        //#if MODERN==0
        net.minecraft.client.settings.KeyBinding
        //#else
            //#if FABRIC==1
            //$$ net.minecraft.client.option.KeyBinding
            //#else
            //$$ net.minecraft.client.KeyMapping
            //#endif
        //#endif

typealias MCEscMenu =
        //#if MODERN==0
        net.minecraft.client.gui.GuiIngameMenu
        //#else
            //#if FABRIC==1
            //$$ net.minecraft.client.gui.screen.GameMenuScreen
            //#else
            //$$ net.minecraft.client.gui.screens.PauseScreen
            //#endif
        //#endif
typealias MCInventoryMenu =
                //#if MODERN==0
                net.minecraft.client.gui.inventory.GuiInventory
                //#else
                    //#if FABRIC==1
                    //$$ net.minecraft.client.gui.screen.ingame.InventoryScreen
                    //#else
                    //$$ net.minecraft.client.gui.screens.inventory.InventoryScreen
                    //#endif
                //#endif
typealias MCChatMenu =
                //#if MODERN==0
                net.minecraft.client.gui.GuiChat
                //#else
                    //#if FABRIC==1
                    //$$ net.minecraft.client.gui.screen.ChatScreen
                    //#else
                    //$$ net.minecraft.client.gui.screens.ChatScreen
                    //#endif
                //#endif

//#if MODERN==0 || FABRIC==1
fun registerKeybinding(name: String, category: String, type: UKeybind.Type, code: Int): MCKeyBinding {
    val bind =
        //#if MODERN==0
        MCKeyBinding(name, if (type == UKeybind.Type.MOUSE) code - 100 else code, category)
        //#else
        //$$ MCKeyBinding(name, when(type) {
        //$$ UKeybind.Type.KEYBOARD -> net.minecraft.client.util.InputUtil.Type.KEYSYM
        //$$ UKeybind.Type.MOUSE -> net.minecraft.client.util.InputUtil.Type.MOUSE
        //$$ else -> net.minecraft.client.util.InputUtil.Type.KEYSYM
        //$$ }, code, category)
        //#endif
    //#if MODERN==0
    net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(bind)
    //#endif
    //#if FABRIC==1
    //$$ net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding(bind)
    //#endif
    return bind
}
//#endif

//#if MODERN==0 || FABRIC==1
fun isPressed(bind: UKeybind): Boolean {
    return bind.getBinding()
        //#if MODERN==0
        .keyCode != UKeyboard.KEY_NONE
        //#else
        //$$ .isUnbound
        //#endif
            && bind.getBinding().
    //#if MODERN==0
    isPressed
    //#else
    //$$ wasPressed()
    //#endif
}
//#endif

fun runOnMcThread(block: () -> Unit) {
    UMinecraft.getMinecraft().
        //#if MODERN==0
        addScheduledTask(block)
        //#else
            //#if FABRIC==1
            //$$ executeTask(block)
            //#else
            //$$ doRunTask(block)
            //#endif
        //#endif
}

fun isGuiHidden(): Boolean {
    return UMinecraft.getMinecraft().
    //#if MODERN==0
    gameSettings
    //#else
    //$$ options
    //#endif
        .
    //#if MODERN==0
    hideGUI
    //#else
        //#if FABRIC==1
        //$$ hudHidden
        //#else
        //$$ hideGui
        //#endif
    //#endif
}
