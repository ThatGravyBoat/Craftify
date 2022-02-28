package tech.thatgravyboat.craftify.ui

import gg.essential.api.EssentialAPI
import gg.essential.api.gui.Slot
import gg.essential.api.utils.GuiUtil
import gg.essential.api.utils.Multithreading
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.Window
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.universal.*
import org.lwjgl.input.Mouse
import tech.thatgravyboat.craftify.Config
import tech.thatgravyboat.craftify.api.SpotifyAPI
import tech.thatgravyboat.craftify.platform.Event
import tech.thatgravyboat.craftify.platform.UKeybind
import tech.thatgravyboat.craftify.types.PlayerState
import tech.thatgravyboat.craftify.ui.enums.Position
import tech.thatgravyboat.craftify.ui.enums.displaying.DisplayMode
import tech.thatgravyboat.craftify.ui.enums.rendering.RenderType
import java.net.URL

object Player {

    private val skipForward = UKeybind("Skip Forward", "Craftify", UKeybind.Type.KEYBOARD, UKeyboard.KEY_NONE)
    private val skipPrevious = UKeybind("Skip Previous", "Craftify", UKeybind.Type.KEYBOARD, UKeyboard.KEY_NONE)
    private val togglePlaying = UKeybind("Toggle Playing", "Craftify", UKeybind.Type.KEYBOARD, UKeyboard.KEY_NONE)
    private val hidePlayer = UKeybind("Toggle Spotify HUD", "Craftify", UKeybind.Type.KEYBOARD, UKeyboard.KEY_NONE)

    private val window = Window(version = ElementaVersion.V1)
    private var player = UIPlayer() childOf window

    private var isPlaying = false
    private var tempHide = false
    private var lastSong = ""

    init {
        changePosition(Position.values()[Config.position])
    }

    fun init() {
        skipForward.register()
        skipPrevious.register()
        togglePlaying.register()
        hidePlayer.register()
    }

    fun stopClient() {
        player.clientStop()
    }

    fun updatePlayer(state: PlayerState) {
        player.updateState(state)
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

    fun changePosition(position: Position) {
        player.setX(position.x(player))
        player.setY(position.y(player))
    }

    fun onRender() {
        if (tempHide) return
        val renderType = RenderType.values()[Config.renderType].canRender(GuiUtil.getOpenedScreen())
        val displayMode = DisplayMode.values()[Config.displayMode].canDisplay(SpotifyAPI.lastState)
        if (displayMode && renderType && Config.enable) {
            window.draw()
        }
    }

    fun onTick() {
        if (Config.firstTime && UMinecraft.getWorld() != null) {
            Config.firstTime = false
            Config.markDirty()
            Config.writeData()

            UChat.chat("")
            UChat.chat("\u00A77-------[\u00A7aCraftify\u00A77]-------")
            UChat.chat("\u00A76This is your first time loading the mod.")
            UChat.chat("\u00A76To setup the mod run \u00A79/craftify\u00A76 and go to the Login category.")
            UChat.chat("\u00A76If you would like to support the creator you can")
            UChat.chat("\u00A76sub to \u00A72ThatGravyBoat\u00A76 on \u00A7cpatreon\u00A76, link in the")
            UChat.chat("\u00A76config you will also get a small cosmetic if you do.")
            UChat.chat("\u00A77----------------------")
            UChat.chat("")
        }
        if (isPressed(skipForward)) {
            Multithreading.runAsync {
                SpotifyAPI.skip(true)
            }
        }
        if (isPressed(skipPrevious)) {
            Multithreading.runAsync {
                SpotifyAPI.skip(false)
            }
        }
        if (isPressed(togglePlaying)) {
            Multithreading.runAsync {
                SpotifyAPI.changePlayingState(!isPlaying)
                stopClient()
            }
        }
        if (isPressed(hidePlayer)) {
            tempHide = !tempHide
        }
    }

    private fun isPressed(bind: UKeybind): Boolean {
        return bind.getBinding().keyCode != UKeyboard.KEY_NONE && bind.getBinding().isPressed
    }

    // XY values taken from GuiScreen go there if anything screws up.
    fun onMouseClicked(event: Event) {
        if (!Config.enable) return
        if (tempHide) return
        val renderType = RenderType.values()[Config.renderType].canRender(GuiUtil.getOpenedScreen())
        val displayMode = DisplayMode.values()[Config.displayMode].canDisplay(SpotifyAPI.lastState)
        if (renderType && displayMode && Config.enable && player.isHovered() && Mouse.getEventButtonState()) {
            val button = Mouse.getEventButton()
            player.mouseClick(UMouse.Scaled.x, UMouse.Scaled.y, button)
            event.cancel()
        }
    }
}
