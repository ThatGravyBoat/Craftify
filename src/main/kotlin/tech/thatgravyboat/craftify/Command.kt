package tech.thatgravyboat.craftify

import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.SubCommand
import gg.essential.api.utils.GuiUtil
import tech.thatgravyboat.craftify.api.SpotifyAPI
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.themes.library.LibraryScreen
import tech.thatgravyboat.craftify.themes.library.LibraryStorage
import tech.thatgravyboat.craftify.themes.library.ScreenshotScreen
import tech.thatgravyboat.craftify.ui.PositionEditorScreen

object Command : Command("craftify") {

    @DefaultHandler
    fun handle() {
        Config.gui()?.let { GuiUtil.open(it) }
    }

    @SubCommand("theme")
    fun theme() {
        ThemeConfig.gui()?.let { GuiUtil.open(it) }
    }

    @SubCommand("library")
    fun library() {
        GuiUtil.open(LibraryScreen())
    }

    @SubCommand("screenshot")
    fun screenshot() {
        GuiUtil.open(ScreenshotScreen)
    }

    @SubCommand("refresh")
    fun refresh() {
        LibraryStorage.refresh()
    }

    @SubCommand("position")
    fun position() {
        GuiUtil.open(PositionEditorScreen())
    }

    @SubCommand("volume")
    fun setVolume(volume: Int) {
        SpotifyAPI.setVolume(volume.coerceIn(0, 100))
    }
}
