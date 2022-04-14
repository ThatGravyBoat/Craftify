package tech.thatgravyboat.craftify

import gg.essential.api.utils.GuiUtil
import gg.essential.vigilance.gui.SettingsGui
import me.kbrewster.eventbus.Subscribe
import tech.thatgravyboat.craftify.api.SpotifyAPI
import tech.thatgravyboat.craftify.platform.isGuiHidden
import tech.thatgravyboat.craftify.ui.Player

//#if MODERN==0
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
//#endif

//#if MODERN==0
@Mod(
    name = "Craftify",
    modid = "craftify",
    version = "1.0.0",
    modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter"
)
//#endif
object Craftify {


    //#if MODERN==0
    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) //todo scream at essential to fix essential in dev env for 1.17+
    //#else
    //$$ fun onInit()
    //#endif
    {
        //#if MODERN==0
        tech.thatgravyboat.cosmetics.Cosmetics.initialize()
        //#endif
        Command.register()
        SpotifyAPI.startPoller()
        Player.init()
        Events
        eventBus.register(this)
    }

    @Subscribe
    fun onFirstLoad(event: TickEvent) {
        Player.onTick()
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
        }
    }
}
