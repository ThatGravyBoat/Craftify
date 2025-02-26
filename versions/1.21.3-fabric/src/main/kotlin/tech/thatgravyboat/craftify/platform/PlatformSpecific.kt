package tech.thatgravyboat.craftify.platform

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil
import net.minecraft.scoreboard.ScoreboardDisplaySlot

typealias MCKeyBinding = net.minecraft.client.option.KeyBinding
typealias MCEscMenu = net.minecraft.client.gui.screen.GameMenuScreen
typealias MCInventoryMenu = net.minecraft.client.gui.screen.ingame.InventoryScreen
typealias MCCreativeMenu = net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen
typealias MCChatMenu = net.minecraft.client.gui.screen.ChatScreen

fun registerKeybinding(name: String, category: String, type: UKeybind.Type, code: Int): MCKeyBinding {
    val bind = MCKeyBinding(name,
        if (type == UKeybind.Type.MOUSE) InputUtil.Type.MOUSE else InputUtil.Type.KEYSYM,
        code,
        category
    )
    KeyBindingHelper.registerKeyBinding(bind)
    return bind
}

fun isPressed(bind: UKeybind): Boolean {
    return bind.getBinding().isPressed
}

fun runOnMcThread(block: () -> Unit) {
    MinecraftClient.getInstance().executeTask(block)
}

fun isGuiHidden(): Boolean {
    return !MinecraftClient.isHudEnabled()
}

fun isDebugGuiOpened(): Boolean {
    return MinecraftClient.getInstance().debugHud.shouldShowDebugHud()
}

fun isTabOpened(): Boolean {
    val mc = MinecraftClient.getInstance()
    return mc.player?.let { player ->
        val obj = player.world.scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.LIST)
        return mc.options.playerListKey.isPressed && (!mc.isIntegratedServerRunning || player.networkHandler.playerList.size > 1 || obj != null)
    } ?: false
}

fun registerCommand(name: String, commands: Map<String, Runnable>) {
    var command = ClientCommandManager.literal(name).executes { commands[""]?.run(); 1 }
    for (entry in commands.filter { it.key != "" }) {
        command = command.then(ClientCommandManager.literal(entry.key).executes { entry.value.run(); 1 })
    }

    ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher, _ ->
        dispatcher.register(command)
    })
}
