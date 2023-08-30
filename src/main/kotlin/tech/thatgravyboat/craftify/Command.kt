package tech.thatgravyboat.craftify

import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.screens.changelog.ChangeLogScreen
import tech.thatgravyboat.craftify.screens.volume.VolumeScreen
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.themes.library.LibraryScreen
import tech.thatgravyboat.craftify.themes.library.ScreenshotScreen
import tech.thatgravyboat.craftify.ui.PositionEditorScreen
import tech.thatgravyboat.craftify.utils.Utils

object Command {

    val command = "craftify"

    val commands = mapOf(
        "" to Runnable { handle() },
        "theme" to Runnable { theme() },
        "library" to Runnable { library() },
        "screenshot" to Runnable { screenshot() },
        "restart" to Runnable { restart() },
        "position" to Runnable { position() },
        "volume" to Runnable { volume() },
        "changelog" to Runnable { changelog() },
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
        Utils.openScreen(VolumeScreen())
    }

    private fun changelog() {
        Utils.fetchString("https://raw.githubusercontent.com/Craftify-Mod/Data/main/changelog.md")?.let {
            Utils.openScreen(ChangeLogScreen(it))
        }
    }
}
