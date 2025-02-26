package tech.thatgravyboat.craftify.ui

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.Window
import gg.essential.elementa.dsl.childOf
import gg.essential.universal.UMatrixStack
import gg.essential.universal.UMouse
import gg.essential.universal.UScreen
import tech.thatgravyboat.craftify.Initializer
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.platform.compat.obsoverlay.ObsOverlayCompat
import tech.thatgravyboat.craftify.services.ads.AdManager
import tech.thatgravyboat.craftify.themes.library.ScreenshotScreen
import tech.thatgravyboat.craftify.ui.enums.Anchor
import tech.thatgravyboat.jukebox.api.state.State

object Player {

    private val window = Window(version = ElementaVersion.V2)
    private var player: UIPlayer? = null

    private var isPlaying = false
    private var tempHide = false

    private fun checkAndInitPlayer() {
        if (player == null) {
            player = UIPlayer() childOf window
            changePosition(Config.anchorPoint)
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

    fun changeSong() {
        AdManager.changeAd()
    }

    fun updatePlayer(state: State) {
        isPlaying = if (state.song.type.isAd()) {
            player?.updateState(AdManager.getAdState(state))
            false
        } else {
            player?.updateState(state)
            state.isPlaying
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
        if (canRender() && Config.musicService != "disabled") {
            checkAndInitPlayer()
            ObsOverlayCompat.draw {
                window.draw(matrix)
            }
        }
    }

    private fun canRender(): Boolean {
        if (UScreen.currentScreen is PositionEditorScreen) return false
        val renderType = Config.renderType.canRender(UScreen.currentScreen)
        val displayMode = Config.displayMode.canDisplay(Initializer.getAPI()?.getState())
        return (UScreen.currentScreen is ScreenshotScreen || (renderType && displayMode)) && Config.musicService != "disabled"
    }

    // XY values taken from GuiScreen go there if anything screws up.
    fun onMouseClicked(button: Int): Boolean {
        if (Config.musicService == "disabled") return false
        if (tempHide) return false
        if (canRender() && player?.isHovered() == true) {
            player?.mouseClick(UMouse.Scaled.x, UMouse.Scaled.y, button)
            return true
        }
        return false
    }
}
