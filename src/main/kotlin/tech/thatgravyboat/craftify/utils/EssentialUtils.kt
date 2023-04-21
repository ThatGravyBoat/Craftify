package tech.thatgravyboat.craftify.utils

import gg.essential.api.EssentialAPI
import gg.essential.api.gui.Slot
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.width
import tech.thatgravyboat.craftify.ui.EmptyImageProvider
import tech.thatgravyboat.jukebox.api.events.EventType
import tech.thatgravyboat.jukebox.api.events.callbacks.SongChangeEvent
import tech.thatgravyboat.jukebox.api.service.BaseService
import java.net.URL
import java.util.concurrent.CompletableFuture

object EssentialUtils {

    private val songChange: (SongChangeEvent) -> Unit = { event ->
        if (event.state.isPlaying && !event.state.song.type.isAd()) {
            val text = event.state.song.title
            val truncatedText = splitText(text)
            val newText = "♪ ${truncatedText}${if (truncatedText.length == text.length) "" else "..."} ♪"
            setServerHostText(newText)
        }
    }

    fun sendNotification(title: String, message: String, image: String? = null, preview: Boolean = true) {
        EssentialAPI.getNotifications().push(
            title = title,
            message = message,
            configure = {
                try {
                    if (!image.isNullOrBlank()) {
                        image.let {
                            val url = URL(it)
                            this.withCustomComponent(
                                if (preview) Slot.PREVIEW else Slot.ACTION,
                                UIImage(CompletableFuture.supplyAsync {
                                    return@supplyAsync SingleImageCache[url] ?: UIImage.get(url).also { img ->
                                        SingleImageCache[url] = img
                                    }
                                }, EmptyImageProvider, EmptyImageProvider).constrain {
                                    width = 25.pixels()
                                    height = 25.pixels()
                                }
                            )
                        }
                    }
                }catch (_: Exception) { }
            }
        )
    }

    fun setupServerAddon(service: BaseService) {
        service.registerListener(EventType.SONG_CHANGE, songChange)
    }

    fun closeServerAddon(service: BaseService) {
        service.unregisterListener(EventType.SONG_CHANGE, songChange)
    }

    private fun setServerHostText(text: String) {
        if (EssentialAPI.getOnboardingData().hasDeniedEssentialTOS()) return
        if (!EssentialAPI.getConfig().essentialFull) return
        try {
            val instance = EssentialAPI.getInstance()
            val connection = instance.javaClass.getMethod("getConnectionManager").invoke(instance)
            val profile = connection.javaClass.getMethod("getProfileManager").invoke(connection)
            val activityType = Class.forName("gg.essential.connectionmanager.common.enums.ActivityType")
            val updateMethod = profile.javaClass.getMethod("updatePlayerActivity", activityType, String::class.java)
            updateMethod.invoke(profile, activityType.getField("PLAYING").get(null), text)
        }catch (_: Exception) {}
    }

    private fun splitText(text: String): String {
        if (text.width() <= 125) return text
        var currWidth = 0.0
        var index = 0

        while (index < text.length) {
            val charWidth = text[index].width()
            if (currWidth + charWidth > 125) break
            currWidth += charWidth
            index++
        }
        return text.substring(0, index)
    }
}