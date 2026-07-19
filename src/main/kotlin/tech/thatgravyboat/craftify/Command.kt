package tech.thatgravyboat.craftify

import com.mojang.brigadier.CommandDispatcher
import gg.essential.universal.UChat
import net.fabricmc.fabric.api.client.command.v2.ClientCommands
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.platform.McClient
import tech.thatgravyboat.craftify.screens.PositionEditorScreen
import tech.thatgravyboat.craftify.screens.ServersScreen
import tech.thatgravyboat.craftify.screens.volume.VolumeScreen
import tech.thatgravyboat.craftify.services.ServiceHelper
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.themes.library.LibraryScreen
import tech.thatgravyboat.craftify.themes.library.ScreenshotScreen
import tech.thatgravyboat.jukebox.api.service.ServiceFunction

object Command {

    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        val theme = ClientCommands.literal("theme").executes { theme(); 1 }
        val library = ClientCommands.literal("library").executes { library(); 1 }
        val screenshot = ClientCommands.literal("screenshot").executes { screenshot(); 1 }
        val restart = ClientCommands.literal("restart").executes { restart(); 1 }
        val position = ClientCommands.literal("position").executes { position(); 1 }
        val volume = ClientCommands.literal("volume").executes { volume(); 1 }
        val servers = ClientCommands.literal("servers").executes { servers(); 1 }

        dispatcher.register(
            ClientCommands.literal("craftify")
                .executes { handle(); 1 }
                .then(theme)
                .then(library)
                .then(screenshot)
                .then(restart)
                .then(position)
                .then(volume)
                .then(servers)
        )
    }

    private fun handle() {
        McClient.screen = Config.gui()
    }

    private fun theme() {
        McClient.screen = ThemeConfig.gui()
    }

    private fun library() {
        McClient.screen = LibraryScreen()
    }

    private fun screenshot() {
        McClient.screen = ScreenshotScreen
    }

    private fun restart() {
        Craftify.reloadService()
    }

    private fun position() {
        McClient.screen = PositionEditorScreen()
    }

    private fun volume() {
        if (ServiceHelper.doesSupport(ServiceFunction.VOLUME)) {
            McClient.screen = VolumeScreen()
        } else {
            UChat.chat("Your currently selected service does not support volume control.")
        }
    }

    private fun servers() {
        McClient.screen = ServersScreen()
    }
}
