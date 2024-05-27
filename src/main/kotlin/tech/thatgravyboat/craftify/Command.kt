package tech.thatgravyboat.craftify

import gg.essential.universal.UChat
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.screens.changelog.ChangeLogScreen
import tech.thatgravyboat.craftify.screens.servers.ServersScreen
import tech.thatgravyboat.craftify.screens.volume.VolumeScreen
import tech.thatgravyboat.craftify.services.ServiceHelper
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.themes.library.LibraryScreen
import tech.thatgravyboat.craftify.themes.library.ScreenshotScreen
import tech.thatgravyboat.craftify.ui.PositionEditorScreen
import tech.thatgravyboat.craftify.utils.Utils
import tech.thatgravyboat.jukebox.api.service.ServiceFunction

object Command {

    const val command = "craftify"

    val commands = mapOf(
        "" to Runnable { handle() },
        "theme" to Runnable { theme() },
        "library" to Runnable { library() },
        "screenshot" to Runnable { screenshot() },
        "restart" to Runnable { restart() },
        "position" to Runnable { position() },
        "volume" to Runnable { volume() },
        "changelog" to Runnable { changelog() },
        "servers" to Runnable { servers() },
    )

    private fun handle() {
        Config.gui()?.let(Utils::openScreen)
    }

    private fun theme() {
        ThemeConfig.gui()?.let(Utils::openScreen)
    }

    private fun library() {
        Utils.openScreen(LibraryScreen())
    }

    private fun screenshot() {
        Utils.openScreen(ScreenshotScreen)
    }

    private fun restart() {
        Initializer.reloadService()
    }

    private fun position() {
        Utils.openScreen(PositionEditorScreen())
    }

    private fun volume() {
        if (ServiceHelper.doesSupport(ServiceFunction.VOLUME)) {
            Utils.openScreen(VolumeScreen())
        } else {
            UChat.chat("Your currently selected service does not support volume control.")
        }
    }

    private fun changelog() {
        Utils.fetchString("https://raw.githubusercontent.com/Craftify-Mod/Data/main/changelog.md")?.let {
            Utils.openScreen(ChangeLogScreen(it))
        }
    }

    private fun servers() {
        Utils.openScreen(ServersScreen())
    }
}
