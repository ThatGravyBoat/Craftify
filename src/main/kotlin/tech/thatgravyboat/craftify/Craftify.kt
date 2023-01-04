package tech.thatgravyboat.craftify

//#if MODERN==0
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
//#endif

//#if MODERN==0
@Mod(
    name = "Craftify",
    modid = "craftify",
    version = "1.0.0",
    modLanguageAdapter = "tech.thatgravyboat.craftify.platform.KotlinLanguageAdapter"
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
        Initializer.init()
    }
}
