package tech.thatgravyboat.craftify.utils

//#if MODERN==0
import gg.essential.api.EssentialAPI
import gg.essential.api.gui.Slot
//#endif
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import tech.thatgravyboat.craftify.ui.EmptyImageProvider
import java.net.URL
import java.util.concurrent.CompletableFuture

object EssentialUtils {

    fun sendNotification(title: String, message: String, image: String? = null, preview: Boolean = true) {
        //#if MODERN==0
        EssentialAPI.getNotifications().push(
            title = title,
            message = message,
            configure = {
                runCatching {
                    image?.let {
                        val url = URL(it)
                        this.withCustomComponent(
                            if (preview) Slot.PREVIEW else Slot.ACTION,
                            UIImage(CompletableFuture.supplyAsync { MemoryImageCache.COVER_IMAGE.getOrSet(url, UIImage::get) }, EmptyImageProvider, EmptyImageProvider).constrain {
                                width = 25.pixels()
                                height = 25.pixels()
                            }
                        )
                    }
                }
            }
        )
        //#endif
    }

}