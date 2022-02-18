package tech.thatgravyboat.craftify

import gg.essential.universal.UChat
import gg.essential.universal.UMinecraft
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import tech.thatgravyboat.cosmetics.Cosmetics
import tech.thatgravyboat.craftify.api.SpotifyAPI
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
        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(Player)
        Command.register()
        ThemeCommand.register()
        SpotifyAPI.startPoller()
    }

    @SubscribeEvent
    fun onFirstLoad(event: TickEvent.ClientTickEvent) {
        if (Config.firstTime && event.phase.equals(TickEvent.Phase.START) && UMinecraft.getWorld() != null) {
            Config.firstTime = false
            Config.markDirty()
            Config.writeData()

            UChat.chat("")
            UChat.chat("\u00A77-------[\u00A7aCraftily\u00A77]-------")
            UChat.chat("\u00A76This is your first time loading the mod.")
            UChat.chat("\u00A76To setup the mod run \u00A79/craftify\u00A76 and go to the Login category.")
            UChat.chat("\u00A76If you would like to support the creator you can")
            UChat.chat("\u00A76sub to \u00A72ThatGravyBoat\u00A76 on \u00A7cpatreon\u00A76, link in the")
            UChat.chat("\u00A76config you will also get a small cosmetic if you do.")
            UChat.chat("\u00A77----------------------")
            UChat.chat("")
        }
    }
}
