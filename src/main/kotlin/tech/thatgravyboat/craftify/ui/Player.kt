package tech.thatgravyboat.craftify.ui

import gg.essential.api.utils.GuiUtil
import gg.essential.api.utils.Multithreading
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.Window
import gg.essential.elementa.dsl.childOf
import gg.essential.universal.UChat
import gg.essential.universal.UMouse
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import tech.thatgravyboat.craftify.Config
import tech.thatgravyboat.craftify.api.SpotifyAPI
import tech.thatgravyboat.craftify.types.PlayerState
import tech.thatgravyboat.craftify.ui.enums.Position
import tech.thatgravyboat.craftify.ui.enums.RenderType


object Player {

    private val skipForward = KeyBinding("Skip Forward", Keyboard.KEY_NONE, "Craftify")
    private val skipPrevious = KeyBinding("Skip Previous", Keyboard.KEY_NONE, "Craftify")
    private val togglePlaying = KeyBinding("Toggle Playing", Keyboard.KEY_NONE, "Craftify")
    private val hidePlayer = KeyBinding("Toggle Spotify HUD", Keyboard.KEY_NONE, "Craftify")

    private val window = Window(version = ElementaVersion.V1)
    private val player = UIPlayer() childOf window

    private var isPlaying = false
    private var tempHide = false
    private var lastSong = ""

    init {
        changePosition(Position.values()[Config.position])
        ClientRegistry.registerKeyBinding(skipForward)
        ClientRegistry.registerKeyBinding(skipPrevious)
        ClientRegistry.registerKeyBinding(togglePlaying)
        ClientRegistry.registerKeyBinding(hidePlayer)
    }

    fun stopClient() {
        player.clientStop()
    }

    fun updatePlayer(state: PlayerState) {
        player.updateState(state)
        isPlaying = state.isPlaying()
        if (lastSong != state.getTitle() && state.isPlaying()) {
            if (Config.announceNewSong) {
                UChat.chat("[Craftify]: Now Playing: ${state.getTitle()}")
            }
        }
        lastSong = state.getTitle()
    }

    fun changePosition(position: Position) {
        player.setX(position.x(player))
        player.setY(position.y(player))
    }

    @SubscribeEvent
    fun onRender(event: TickEvent.RenderTickEvent) {
        if (event.phase.equals(TickEvent.Phase.START)) return
        if (tempHide) return
        val renderType = RenderType.values()[Config.renderType]
        if (renderType.canRender(GuiUtil.getOpenedScreen()) && Config.enable) {
            window.draw()
        }
    }

    @SubscribeEvent
    fun onKeyInput(event: KeyInputEvent) {
        if (Keyboard.getEventKeyState()) {
            when (Keyboard.getEventKey()) {
                skipForward.keyCode -> {
                    Multithreading.runAsync {
                        SpotifyAPI.skip(true)
                    }
                }
                skipPrevious.keyCode -> {
                    Multithreading.runAsync {
                        SpotifyAPI.skip(false)
                    }
                }
                togglePlaying.keyCode -> {
                    Multithreading.runAsync {
                        SpotifyAPI.changePlayingState(!isPlaying)
                        stopClient()
                    }
                }
                hidePlayer.keyCode -> {
                    tempHide = !tempHide
                }
            }
        }
    }

    // XY values taken from GuiScreen go there if anything screws up.
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onMouseClicked(event: GuiScreenEvent.MouseInputEvent.Pre) {
        if (!Config.enable) return
        if (tempHide) return
        val renderType = RenderType.values()[Config.renderType]
        if (renderType.canRender(GuiUtil.getOpenedScreen()) && Config.enable && player.isHovered() && Mouse.getEventButtonState()) {
            val button = Mouse.getEventButton()
            player.mouseClick(UMouse.Scaled.x, UMouse.Scaled.y, button)
            event.isCanceled = true
        }
    }
}
