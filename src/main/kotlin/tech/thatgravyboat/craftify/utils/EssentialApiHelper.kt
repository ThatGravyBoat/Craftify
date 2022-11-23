package tech.thatgravyboat.craftify.utils

import gg.essential.api.EssentialAPI
import gg.essential.api.gui.Slot
import gg.essential.api.utils.Multithreading
import gg.essential.api.utils.WebUtil
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.universal.UChat
import gg.essential.universal.UDesktop
import gg.essential.universal.UScreen
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Contains all methods that are related to the Essential API, so it can be easily removed if needed.
 */
object EssentialApiHelper {

    /**
     * Opens the screen 1 tick after this is run to make sure the chat doesn't close it.
     */
    fun openScreen(screen: UScreen) {
        EssentialAPI.getGuiUtil().openScreen(screen)
    }

    fun schedule(delay: Long, unit: TimeUnit, runnable: () -> Unit) {
        Multithreading.schedule(runnable, delay, unit)
    }

    fun schedule(start: Long, delay: Long, unit: TimeUnit, runnable: () -> Unit) {
        Multithreading.schedule(runnable, start, delay, unit)
    }

    fun submit(runnable: () -> Unit): Future<*> {
        return Multithreading.submit(runnable)
    }

    fun async(runnable: () -> Unit) {
        Multithreading.runAsync(runnable)
    }

    fun sendNotification(title: String, message: String, image: String? = null, preview: Boolean = true) {
        EssentialAPI.getNotifications().push(
            title = title,
            message = message,
            configure = {
                image?.let {it ->
                    this.withCustomComponent(
                        if (preview) Slot.PREVIEW else Slot.ACTION,
                        UIImage.ofURL(URL(it)).constrain {
                            width = 25.pixels()
                            height = 25.pixels()
                        }
                    )
                }
            }
        )
    }

    fun fetchString(url: String): String? {
        return WebUtil.fetchString(url)
    }

    fun downloadToFile(url: String, file: File, userAgent: String) {
        WebUtil.downloadToFile(url, file, userAgent)
    }

    fun browse(url: String): Boolean {
        return when {
            UDesktop.isLinux -> listOf("xdg-open", "kde-open", "gnome-open").any { runCommand(it, url, checkExitStatus = true) }
            UDesktop.isMac -> runCommand("open", url)
            UDesktop.isWindows -> runCommand("rundll32", "url.dll,FileProtocolHandler", url)
            else -> false
        }
    }

    fun openUrl(url: String) {
        if (!browse(url)) {
            UChat.chat("Failed to open URL: $url")
        }
    }

    /**
     * Taken from UDesktop because it's private
     */
    private fun runCommand(vararg command: String, checkExitStatus: Boolean = false): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(command) ?: return false
            if (checkExitStatus) {
                if (process.waitFor(3, TimeUnit.SECONDS)) {
                    process.exitValue() == 0
                } else {
                    true // still running, assume success
                }
            } else {
                process.isAlive
            }
        } catch (e: IOException) {
            false
        }
    }
}