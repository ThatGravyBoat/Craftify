package tech.thatgravyboat.craftify

import gg.essential.universal.UChat
import gg.essential.universal.UKeyboard
import gg.essential.universal.UMatrixStack
import gg.essential.universal.UScreen
import gg.essential.universal.utils.MCScreen
import gg.essential.universal.wrappers.UPlayer
import gg.essential.vigilance.gui.SettingsGui
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.platform.*
import tech.thatgravyboat.craftify.services.ServiceHelper.close
import tech.thatgravyboat.craftify.services.ServiceHelper.setup
import tech.thatgravyboat.craftify.services.ServiceType
import tech.thatgravyboat.craftify.services.ads.AdManager
import tech.thatgravyboat.craftify.services.update.Updater
import tech.thatgravyboat.craftify.ssl.FixSSL
import tech.thatgravyboat.craftify.ui.Player
import tech.thatgravyboat.craftify.utils.Utils
import tech.thatgravyboat.jukebox.api.service.BaseService
import tech.thatgravyboat.jukebox.api.service.Service

object Initializer {

    private val skipForward = UKeybind("Skip Forward", "Craftify", UKeybind.Type.KEYBOARD, UKeyboard.KEY_NONE)
    private val skipPrevious = UKeybind("Skip Previous", "Craftify", UKeybind.Type.KEYBOARD, UKeyboard.KEY_NONE)
    private val togglePlaying = UKeybind("Toggle Playing", "Craftify", UKeybind.Type.KEYBOARD, UKeyboard.KEY_NONE)
    private val hidePlayer = UKeybind("Toggle Craftify HUD", "Craftify", UKeybind.Type.KEYBOARD, UKeyboard.KEY_NONE)

    private var inited = false

    private var api: BaseService? = null

    fun init() {

        //#if MODERN==0
        FixSSL.fixup()
        Utils.setupJukeboxHttp()
        //#endif
        Utils.isEssentialInstalled // Load lazy
        Updater.check()
        AdManager.load()

        Events.TICK.register { onTick() }
        Events.RENDER.register { onRender(it) }
        Events.MOUSE_CLICKED.register { onMouseClicked(it) }
        Events.SCREEN_CHANGED.register { onScreenChanged(it) }

        //#if MODERN==0
        tech.thatgravyboat.cosmetics.Cosmetics.initialize()
        //#endif
        reloadService()

        skipForward.register()
        skipPrevious.register()
        togglePlaying.register()
        hidePlayer.register()
        EventHandler
    }

    private fun onTick() {
        if (!inited) {
            registerCommand(Command.command, Command.commands)
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
        if (Updater.hasUpdate() && UPlayer.hasPlayer()) {
            Updater.showMessage()
        }
        if (isPressed(skipForward)) {
            Utils.async {
                api?.move(true)
            }
        }
        if (isPressed(skipPrevious)) {
            Utils.async {
                api?.move(false)
            }
        }
        if (isPressed(togglePlaying)) {
            Utils.async {
                api?.setPaused(Player.isPlaying())
                Player.stopClient()
            }
        }
        if (isPressed(hidePlayer)) {
            Player.toggleHiding()
        }
        Utils.getOpenScreen()?.let {
            UScreen.displayScreen(it)
        }
    }

    private fun onRender(matrix: UMatrixStack) {
        if (isGuiHidden()) return
        Player.onRender(matrix)
    }

    private fun onMouseClicked(button: Int): Boolean {
        return Player.onMouseClicked(button)
    }

    private fun onScreenChanged(screen: MCScreen?) {
        if (screen == null && UScreen.currentScreen is SettingsGui) {
            Player.updateTheme()
            val service = Config.getService()
            if (service == "disabled") {
                api?.stop()
                api?.close()
                api = null
            } else {
                reloadService()
            }
        }
    }

    fun reloadService() {
        val service = Config.getService().takeIf { it != "disabled" } ?: return
        val type = ServiceType.fromId(service) ?: return

        api?.stop()
        api?.close()
        api = type.create()
        api?.start()
        api?.setup()
    }

    fun getAPI(): Service? {
        return api
    }
}