package tech.thatgravyboat.craftify

import gg.essential.api.utils.GuiUtil
import gg.essential.vigilance.gui.SettingsGui
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import tech.thatgravyboat.cosmetics.Cosmetics
import tech.thatgravyboat.craftify.api.SpotifyAPI
import tech.thatgravyboat.craftify.platform.Event
import tech.thatgravyboat.craftify.themes.ThemeCommand
import tech.thatgravyboat.craftify.ui.Player

@Mod(
    name = "Craftify",
    modid = "craftify",
    version = "1.0.0",
    modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter"
)
object Craftify {

    @Mod.EventHandler
    fun onFMLInitialization(event: FMLInitializationEvent?) {
        Cosmetics.initialize()
        Command.register()
        ThemeCommand.register()
        SpotifyAPI.startPoller()
        Player.init()
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onFirstLoad(event: TickEvent.ClientTickEvent) {
        if (event.phase.equals(TickEvent.Phase.START)) return
        Player.onTick()
    }

    @SubscribeEvent
    fun onRender(event: TickEvent.RenderTickEvent) {
        if (event.phase.equals(TickEvent.Phase.START)) return
        Player.onRender()
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onMouseClicked(mouseEvent: GuiScreenEvent.MouseInputEvent.Pre) {
        val event = Event()
        Player.onMouseClicked(event)
        if (event.isCancelled()) mouseEvent.isCanceled = true
    }

    @SubscribeEvent
    fun onGuiClose(event: GuiOpenEvent) {
        if (event.gui == null && GuiUtil.getOpenedScreen() is SettingsGui) {
            Player.updateTheme()
        }
    }
}
