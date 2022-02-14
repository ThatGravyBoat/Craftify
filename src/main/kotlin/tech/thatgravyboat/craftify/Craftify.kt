package tech.thatgravyboat.craftify

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
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
}
