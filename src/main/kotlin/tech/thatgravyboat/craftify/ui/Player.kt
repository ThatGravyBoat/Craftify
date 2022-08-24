package tech.thatgravyboat.craftify.ui

import gg.essential.api.EssentialAPI
import gg.essential.api.gui.Slot
import gg.essential.api.utils.GuiUtil
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.Window
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.universal.ChatColor
import gg.essential.universal.UChat
import gg.essential.universal.UMatrixStack
import gg.essential.universal.UMouse
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.Initializer
import tech.thatgravyboat.craftify.platform.MouseClickEvent
import tech.thatgravyboat.craftify.themes.library.ScreenshotScreen
import tech.thatgravyboat.craftify.types.PlayerState
import tech.thatgravyboat.craftify.ui.enums.Anchor
import tech.thatgravyboat.craftify.ui.enums.displaying.DisplayMode
import tech.thatgravyboat.craftify.ui.enums.rendering.RenderType
import java.net.URL

object Player {

    private val window = Window(version = ElementaVersion.V1)
    private var player: UIPlayer? = null

    private var isPlaying = false
    private var tempHide = false
    private var lastSong = ""

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

    fun updatePlayer(state: PlayerState) {
        player?.updateState(state)
        isPlaying = state.isPlaying()
        if (lastSong != state.getTitle() && state.isPlaying() && state.hasData()) {
            if (Config.announceNewSong == 1) {
                UChat.chat(
                    "${ChatColor.GREEN}Craftify > ${ChatColor.GRAY}" +
                        "Now Playing: ${ChatColor.AQUA}${state.getTitle()} by ${state.getArtists()}"
                )
            }
            if (Config.announceNewSong == 2) {
                EssentialAPI.getNotifications().push(
                    title = "Craftify",
                    message = "Now Playing: \n${state.getTitle()}",
                    configure = {
                        if (Config.announcementRendering != 0) {
                            this.withCustomComponent(
                                if (Config.announcementRendering == 1) Slot.PREVIEW else Slot.ACTION,
                                UIImage.ofURL(URL(state.getImage())).constrain {
                                    width = 25.pixels()
                                    height = 25.pixels()
                                }
                            )
                        }
                    }
                )
            }
        }
        lastSong = state.getTitle()
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
        if (GuiUtil.getOpenedScreen() is PositionEditorScreen) return false
        val renderType = RenderType.values()[Config.renderType].canRender(GuiUtil.getOpenedScreen())
        val displayMode = DisplayMode.values()[Config.displayMode].canDisplay(Initializer.getAPI()?.getState())
        return (GuiUtil.getOpenedScreen() is ScreenshotScreen || (renderType && displayMode)) && Config.modMode != 0
    }

    // XY values taken from GuiScreen go there if anything screws up.
    fun onMouseClicked(event: MouseClickEvent) {
        if (Config.modMode == 0) return
        if (tempHide) return
        if (canRender() && player?.isHovered() == true) {
            player?.mouseClick(UMouse.Scaled.x, UMouse.Scaled.y, event.button)
            event.cancelled = true
        }
    }
}
