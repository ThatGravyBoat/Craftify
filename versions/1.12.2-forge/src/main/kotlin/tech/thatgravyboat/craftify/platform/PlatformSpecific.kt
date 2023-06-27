package tech.thatgravyboat.craftify.platform

import gg.essential.elementa.ElementaVersion
import gg.essential.universal.UKeyboard
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.scoreboard.ScoreObjective
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.fml.client.registry.ClientRegistry

typealias MCKeyBinding = net.minecraft.client.settings.KeyBinding
typealias MCEscMenu = net.minecraft.client.gui.GuiIngameMenu
typealias MCInventoryMenu = net.minecraft.client.gui.inventory.GuiInventory
typealias MCCreativeMenu = net.minecraft.client.gui.inventory.GuiContainerCreative
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

fun isDebugGuiOpened(): Boolean {
    return Minecraft.getMinecraft().gameSettings.showDebugInfo
}

fun isTabOpened(): Boolean {
    val mc = Minecraft.getMinecraft()
    val handler = mc.player.connection
    val obj: ScoreObjective? = mc.world.scoreboard.getObjectiveInDisplaySlot(0)
    return mc.gameSettings.keyBindPlayerList.isKeyDown && (!mc.isIntegratedServerRunning || handler.playerInfoMap.size > 1 || obj != null)
}

fun registerCommand(name: String, commands: Map<String, Runnable>) {
    ClientCommandHandler.instance.registerCommand(SimpleCommand(
        name,
        { commands[""]?.run() },
        commands.filter { it.key != "" }
    ))
}

class SimpleCommand(private val name: String, private val default: Runnable, private val commands: Map<String, Runnable>): CommandBase() {

    override fun getName() = name

    override fun getUsage(sender: ICommandSender) = "/$name"

    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<out String>) {
        if (args.isEmpty()) {
            default.run()
        } else {
            commands[args[0]]?.run()
        }
    }

    override fun checkPermission(server: MinecraftServer, sender: ICommandSender) = true

    override fun getTabCompletions(server: MinecraftServer, sender: ICommandSender, args: Array<out String>, targetPos: BlockPos?) = if (args.isEmpty()) commands.keys.toMutableList() else mutableListOf()
}
