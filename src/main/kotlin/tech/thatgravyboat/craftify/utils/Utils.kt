package tech.thatgravyboat.craftify.utils

import gg.essential.universal.UChat
import gg.essential.universal.UDesktop
import gg.essential.universal.UScreen
import gg.essential.universal.utils.MCScreen
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import tech.thatgravyboat.craftify.ssl.FixSSL
import tech.thatgravyboat.jukebox.utils.Http
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.security.cert.X509Certificate
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.X509TrustManager

object Utils {

    private var isEssentialInstalled = false

    private var toOpen: MCScreen? = null

    private val counter = AtomicInteger(0)

    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(10) { target: Runnable? ->
        Thread(target, "Craftify Thread " + counter.incrementAndGet())
    }

    private var executor = ThreadPoolExecutor(10, 30, 0L, TimeUnit.SECONDS, LinkedBlockingQueue()) { target: Runnable? ->
        Thread(target, "Craftify ${counter.incrementAndGet()}")
    }

    fun setupJukeboxHttp() {
        Http.setClient(HttpClient(CIO) {
            BrowserUserAgent()
            install(HttpTimeout)
            engine {
                https {
                    // TODO This is a really bad way to do this but ktor CIO doesnt support socket context
                    trustManager = object: X509TrustManager {
                        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                        override fun getAcceptedIssuers() = null
                    }
                }
            }
        })
    }

    /**
     * Opens the screen 1 tick after this is run to make sure the chat doesn't close it.
     */
    fun openScreen(screen: UScreen) {
        toOpen = screen
    }

    fun getOpenScreen(): MCScreen? {
        val temp = toOpen
        toOpen = null
        return temp
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

    fun login(url: String, auth: String? = null, body: String? = null, contentType: String? = null): Response? {
        return try {
            val bytes: ByteArray? = body?.toByteArray(StandardCharsets.UTF_8)
            return setupUrl(url, method = "POST") {
                contentType?.apply { it.addRequestProperty("Content-Type", this) }
                auth?.apply { it.addRequestProperty("Authorization", "Bearer $this") }
                bytes?.apply {
                    it.addRequestProperty("Content-Length", size.toString())
                    it.doInput = true
                    it.outputStream.write(this)
                }
            }
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

    fun downloadToFile(url: String, file: File) {
        try {
            setupUrl(url).use { setup ->
                FileUtils.copyInputStreamToFile(setup, file)
            }
        }catch (e: Exception) {
            println("Failed to download from $url")
            e.printStackTrace()
        }
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
        if (connection is HttpsURLConnection && FixSSL.context != null) {
            connection.sslSocketFactory = FixSSL.context!!.socketFactory
        }
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

    /**
     * Taken from UDesktop because it's private
     */
    private fun runCommand(vararg command: String, checkExitStatus: Boolean = false): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(command) ?: return false
            if (checkExitStatus) {
                return !process.waitFor(3, TimeUnit.SECONDS) || process.exitValue() == 0
            } else {
                process.isAlive
            }
        } catch (e: IOException) {
            false
        }
    }

    fun String.clearFormatting(): String {
        return this.replace("&([0-9a-fk-or])".toRegex(), "&&r$1")
    }

    fun isEssentialInstalled() = isEssentialInstalled

    internal fun checkEssential() {
        isEssentialInstalled = try {
            Class.forName("gg.essential.api.EssentialAPI")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }
}