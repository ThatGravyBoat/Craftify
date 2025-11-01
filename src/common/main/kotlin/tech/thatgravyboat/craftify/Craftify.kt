package tech.thatgravyboat.craftify

import com.mojang.blaze3d.platform.InputConstants
import gg.essential.universal.UChat
import gg.essential.universal.UKeyboard
import gg.essential.universal.UScreen
import gg.essential.vigilance.gui.SettingsGui
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.client.gui.screens.Screen
import net.minecraft.resources.ResourceLocation
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.platform.McClient
import tech.thatgravyboat.craftify.platform.McKeybind
import tech.thatgravyboat.craftify.platform.ServerboundSongPacket
import tech.thatgravyboat.craftify.services.ServiceHelper.close
import tech.thatgravyboat.craftify.services.ServiceHelper.setup
import tech.thatgravyboat.craftify.services.ServiceTypes
import tech.thatgravyboat.craftify.services.ads.AdManager
import tech.thatgravyboat.craftify.services.config.ServiceConfig
import tech.thatgravyboat.craftify.services.update.Updater
import tech.thatgravyboat.craftify.ui.Player
import tech.thatgravyboat.craftify.utils.Utils
import tech.thatgravyboat.jukebox.api.service.BaseService

object Craftify : ClientModInitializer {

    private val keyBindCategory = ResourceLocation.fromNamespaceAndPath("craftify", "craftify")
    private val skipForward = McKeybind("Skip Forward", keyBindCategory, InputConstants.Type.KEYSYM, UKeyboard.KEY_NONE)
    private val skipPrevious = McKeybind("Skip Previous", keyBindCategory, InputConstants.Type.KEYSYM, UKeyboard.KEY_NONE)
    private val togglePlaying = McKeybind("Toggle Playing", keyBindCategory, InputConstants.Type.KEYSYM, UKeyboard.KEY_NONE)
    private val hidePlayer = McKeybind("Toggle Craftify HUD", keyBindCategory, InputConstants.Type.KEYSYM, UKeyboard.KEY_NONE)

    var service: BaseService? = null
        private set

    private var initalized: Boolean = false

    override fun onInitializeClient() {
        Updater.check()
        AdManager.load()
        ServiceConfig.load()

        skipForward.register()
        skipPrevious.register()
        togglePlaying.register()
        hidePlayer.register()

        ClientTickEvents.END_CLIENT_TICK.register { onTick() }
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ -> Command.register(dispatcher) }

        PayloadTypeRegistry.playC2S().register(ServerboundSongPacket.TYPE, ServerboundSongPacket.CODEC)
        PayloadTypeRegistry.configurationC2S().register(ServerboundSongPacket.TYPE, ServerboundSongPacket.CODEC)
    }

    private fun onTick() {
        if (Config.firstTime && McClient.player != null) {
            Config.firstTime = false
            Config.markDirty()
            Config.writeData()

            val message = """
                §7-------[§aCraftify§7]-------
                §6This is your first time loading the mod.
                §6To setup the mod run §9/craftify§6 and go to the Login category.
                §6If you would like to support the creator you can
                §6sub to §2ThatGravyBoat§6 on §fGitHub§6, link in the
                §6config.
                §7----------------------
            """

            UChat.chat(message.trimIndent())
        }

        if (!this.initalized) {
            this.initalized = true
            reloadService()
        }

        if (Updater.hasUpdate() && McClient.player != null) Updater.showMessage()
        if (skipForward.isPressed) Utils.async { service?.move(true) }
        if (skipPrevious.isPressed) Utils.async { service?.move(false) }
        if (togglePlaying.isPressed) Utils.async { service?.setPaused(Player.state?.isPlaying == true) }
        if (hidePlayer.isPressed) Player.toggleHiding()
    }

    fun onScreenChanged(screen: Screen?) {
        if (screen == null && UScreen.currentScreen is SettingsGui) {
            Config.markDirty()

            if (Config.musicService == "disabled") {
                service?.stop()
                service?.close()
                service = null
            } else {
                reloadService()
            }
        }
    }

    fun reloadService() {
        val type = ServiceTypes.fromId(Config.musicService) ?: return

        service?.stop()
        service?.close()
        service = type.createOrReuse(service)
        service?.start()
        service?.setup()
    }

    fun id(path: String) = ResourceLocation.fromNamespaceAndPath("craftify", path)
}