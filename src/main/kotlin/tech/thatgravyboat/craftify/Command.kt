package tech.thatgravyboat.craftify

import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.SubCommand
import gg.essential.api.utils.WebUtil
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.screens.changelog.ChangeLogScreen
import tech.thatgravyboat.craftify.screens.volume.VolumeScreen
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.themes.library.LibraryScreen
import tech.thatgravyboat.craftify.themes.library.LibraryStorage
import tech.thatgravyboat.craftify.themes.library.ScreenshotScreen
import tech.thatgravyboat.craftify.ui.PositionEditorScreen
import tech.thatgravyboat.craftify.utils.EssentialApiHelper
import java.util.*

object Command : Command("craftify") {

    @DefaultHandler
    fun handle() {
        Config.gui()?.let { EssentialApiHelper.openScreen(it) }
    }

    @SubCommand("theme")
    fun theme() {
        ThemeConfig.gui()?.let { EssentialApiHelper.openScreen(it) }
    }

    @SubCommand("library")
    fun library() {
        EssentialApiHelper.openScreen(LibraryScreen())
    }

    @SubCommand("screenshot")
    fun screenshot() {
        EssentialApiHelper.openScreen(ScreenshotScreen)
    }

    @SubCommand("refresh")
    fun refresh() {
        LibraryStorage.refresh()
    }

    @SubCommand("position")
    fun position() {
        EssentialApiHelper.openScreen(PositionEditorScreen())
    }

    @SubCommand("volume")
    fun setVolume(volume: Optional<Int>) {
        if (volume.isPresent) {
            Initializer.getAPI()?.setVolume(volume.get().coerceIn(0, 100), true)
        } else {
            EssentialApiHelper.openScreen(VolumeScreen())
        }
    }

    @SubCommand("changelog")
    fun changelog() {
        WebUtil.fetchString("https://raw.githubusercontent.com/ThatGravyBoat/craftify-data/main/changelog.md")?.let {
            EssentialApiHelper.openScreen(ChangeLogScreen(it))
        }
    }
}
