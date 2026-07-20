package tech.thatgravyboat.craftify.utils

import gg.essential.universal.UChat
import gg.essential.universal.UDesktop
import org.apache.commons.io.IOUtils
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

object Utils {

    private val counter = AtomicInteger(0)

    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(10) { target: Runnable? ->
        Thread(target, "Craftify Thread " + counter.incrementAndGet()).apply { isDaemon = false }
    }

    private var executor = ThreadPoolExecutor(10, 30, 0L, TimeUnit.SECONDS, LinkedBlockingQueue()) { target: Runnable? ->
        Thread(target, "Craftify ${counter.incrementAndGet()}").apply { isDaemon = false }
    }

    fun schedule(delay: Long, unit: TimeUnit, runnable: Runnable): ScheduledFuture<*>
        = scheduler.schedule(runnable, delay, unit)

    fun schedule(start: Long, delay: Long, unit: TimeUnit, runnable: Runnable): ScheduledFuture<*>
        = scheduler.scheduleAtFixedRate(runnable, start, delay, unit)

    fun submit(runnable: Runnable): Future<*> {
        return executor.submit(runnable)
    }

    fun async(runnable: Runnable) {
        executor.execute(runnable)
    }

    fun login(url: String): Response? {
        return try {
            return setupUrl(url, method = "POST") {}
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun fetchString(url: String): String? {
        try {
            setupUrl(url).use { setup ->
                return IOUtils.toString(setup, Charset.defaultCharset())
            }
        } catch (e: Exception) {
            println("Failed to fetch from $url")
            e.printStackTrace()
        }
        return null
    }

    private fun setupUrl(url: String, method: String = "GET", setup: (HttpURLConnection) -> Unit): Response {
        val escapedUrl = url.replace(" ", "%20")
        val connection = URL(escapedUrl).openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.useCaches = true
        connection.addRequestProperty("User-Agent", "Mozilla/4.76 (Craftify)")
        connection.readTimeout = 15000
        connection.connectTimeout = 15000
        connection.doOutput = true
        setup(connection)
        if (connection.responseCode / 100 != 2) {
            return Response(connection.errorStream, connection.responseCode)
        }
        return Response(connection.inputStream, connection.responseCode)
    }

    private fun setupUrl(url: String): InputStream {
        return setupUrl(url) { }.inputStream
    }

    data class Response(val inputStream: InputStream, val responseCode: Int) {

        fun success(): Boolean {
            return responseCode / 100 == 2
        }
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

    fun runCommand(vararg command: String, checkExitStatus: Boolean = false): Boolean {
        return runCatching {
            val process = Runtime.getRuntime().exec(command) ?: error("Failed to execute command")
            if (checkExitStatus) {
                !process.waitFor(3, TimeUnit.SECONDS) || process.exitValue() == 0
            } else {
                process.isAlive
            }
        }.getOrDefault(false)
    }

    fun execCommand(vararg command: String): Result<String> {
        return runCatching {
            val process = Runtime.getRuntime().exec(command) ?: error("Failed to execute command")

            IOUtils.toString(process.inputStream, StandardCharsets.UTF_8)
        }
    }
}