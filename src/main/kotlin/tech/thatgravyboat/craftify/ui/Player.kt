package tech.thatgravyboat.craftify.ui

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.Window
import gg.essential.elementa.dsl.childOf
import gg.essential.universal.*
import tech.thatgravyboat.craftify.Initializer
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.services.AdManager
import tech.thatgravyboat.craftify.themes.library.ScreenshotScreen
import tech.thatgravyboat.craftify.ui.enums.Anchor
import tech.thatgravyboat.craftify.ui.enums.DisplayMode
import tech.thatgravyboat.craftify.ui.enums.RenderType
import tech.thatgravyboat.craftify.utils.EssentialUtils
import tech.thatgravyboat.craftify.utils.Utils
import tech.thatgravyboat.jukebox.api.state.State

object Player {

    private val window = Window(version = ElementaVersion.V2)
    private var player: UIPlayer? = null

    private var isPlaying = false
    private var tempHide = false

    private fun checkAndInitPlayer() {
        if (player == null) {
            player = UIPlayer() childOf window
            changePosition(Anchor.values()[Config.anchorPoint])
            updateTheme()
            Initializer.getAPI()?.getState()?.let {
                player?.updateState(it)
            }
        }
    }

    fun isPlaying(): Boolean {
        return isPlaying
    }

    fun toggleHiding() {
        tempHide = !tempHide
    }

    fun updateTheme() {
        player?.updateTheme()
    }

    fun stopClient() {
        player?.clientStop()
    }

    fun changeSong(state: State) {
        AdManager.changeAd()
        if (Config.announceNewSong == 1) {
            UChat.chat(
                Config.announcementMessage
                        .replace("%SONG%", state.song.title)
                        .replace("%ARTISTS%", state.song.artists.joinToString( ","))
            )
        }
        if (Config.announceNewSong == 2 && Utils.isEssentialInstalled()) {
            EssentialUtils.sendNotification(
                "Craftify",
                "Now Playing: \n${state.song.title}",
                if (Config.announcementRendering != 0) state.song.cover else null,
                Config.announcementRendering == 1
            )
        }
    }

    fun updatePlayer(state: State) {
        if (state.song.type.isAd()) {
            player?.updateState(AdManager.getAdState(state))
            isPlaying = false
        } else {
            player?.updateState(state)
            isPlaying = state.isPlaying
        }
    }

    fun changePosition(position: Anchor) {
        player?.apply {
            setX(position.getX(this@apply))
            setY(position.getY(this@apply))
        }
    }

    fun onRender(matrix: UMatrixStack) {
        if (tempHide) return
        if (canRender() && Config.modMode != 0) {
            checkAndInitPlayer()
            window.draw(matrix)
        }
    }

    private fun canRender(): Boolean {
        if (UScreen.currentScreen is PositionEditorScreen) return false
        val renderType = RenderType.values()[Config.renderType].canRender(UScreen.currentScreen)
        val displayMode = DisplayMode.values()[Config.displayMode].canDisplay(Initializer.getAPI()?.getState())
        return (UScreen.currentScreen is ScreenshotScreen || (renderType && displayMode)) && Config.modMode != 0
    }

    // XY values taken from GuiScreen go there if anything screws up.
    fun onMouseClicked(button: Int): Boolean {
        if (Config.modMode == 0) return false
        if (tempHide) return false
        if (canRender() && player?.isHovered() == true) {
            player?.mouseClick(UMouse.Scaled.x, UMouse.Scaled.y, button)
            return true
        }
        return false
    }
}
