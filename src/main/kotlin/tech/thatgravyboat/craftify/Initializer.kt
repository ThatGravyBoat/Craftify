package tech.thatgravyboat.craftify

import gg.essential.api.utils.GuiUtil
import gg.essential.api.utils.Multithreading
import gg.essential.universal.UChat
import gg.essential.universal.UKeyboard
import gg.essential.universal.wrappers.UPlayer
import gg.essential.vigilance.gui.SettingsGui
import me.kbrewster.eventbus.Subscribe
import tech.thatgravyboat.craftify.services.SpotifyAPI
import tech.thatgravyboat.craftify.platform.*
import tech.thatgravyboat.craftify.services.BaseAPI
import tech.thatgravyboat.craftify.services.YtmdAPI
import tech.thatgravyboat.craftify.ui.Player

object Initializer {

    private val skipForward = UKeybind("Skip Forward", "Craftify", UKeybind.Type.KEYBOARD, UKeyboard.KEY_NONE)
    private val skipPrevious = UKeybind("Skip Previous", "Craftify", UKeybind.Type.KEYBOARD, UKeyboard.KEY_NONE)
    private val togglePlaying = UKeybind("Toggle Playing", "Craftify", UKeybind.Type.KEYBOARD, UKeyboard.KEY_NONE)
    private val hidePlayer = UKeybind("Toggle Spotify HUD", "Craftify", UKeybind.Type.KEYBOARD, UKeyboard.KEY_NONE)

    private var inited = false

    private var api: BaseAPI? = null

    fun init() {
        //#if MODERN==0
        tech.thatgravyboat.cosmetics.Cosmetics.initialize()
        //#endif
        Config
        if (Config.modMode == 1) {
            api = SpotifyAPI
        }
        if (Config.modMode == 2) {
            api = YtmdAPI
        }
        api?.startPoller()
        skipForward.register()
        skipPrevious.register()
        togglePlaying.register()
        hidePlayer.register()
        EventHandler
        eventBus.register(this)
    }

    @Subscribe
    fun onFirstLoad(event: TickEvent) {
        if (!inited) {
            Command.register()
            inited = true
        }
        if (Config.firstTime && UPlayer.hasPlayer()) {
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
                api?.skip(true)
            }
        }
        if (isPressed(skipPrevious)) {
            Multithreading.runAsync {
                api?.skip(false)
            }
        }
        if (isPressed(togglePlaying)) {
            Multithreading.runAsync {
                api?.changePlayingState(!Player.isPlaying())
                Player.stopClient()
            }
        }
        if (isPressed(hidePlayer)) {
            Player.toggleHiding()
        }
    }

    @Subscribe
    fun onRender(event: RenderEvent) {
        if (isGuiHidden()) return
        Player.onRender(event.matrixStack)
    }

    @Subscribe
    fun onMouseClicked(mouseEvent: MouseClickEvent) {
        Player.onMouseClicked(mouseEvent)
    }

    @Subscribe
    fun onGuiClose(event: ScreenOpenEvent) {
        if (event.gui == null && GuiUtil.getOpenedScreen() is SettingsGui) {
            Player.updateTheme()
            if (Config.modMode == 0) api?.stopPoller()
            if (api == SpotifyAPI && Config.modMode == 2) {
                SpotifyAPI.stopPoller()
                api = YtmdAPI
            }
            if (api == YtmdAPI && Config.modMode == 1) {
                YtmdAPI.stopPoller()
                api = SpotifyAPI
            }
            if (Config.modMode != 0) api?.restartPoller()
        }
    }

    fun getAPI(): BaseAPI? {
        return api
    }
}