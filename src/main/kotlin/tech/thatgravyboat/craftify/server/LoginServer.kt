package tech.thatgravyboat.craftify.server

import com.sun.net.httpserver.HttpServer
import gg.essential.api.utils.Multithreading
import gg.essential.universal.UChat
import gg.essential.universal.UDesktop.browse
import java.net.InetSocketAddress
import java.net.URI
import java.util.concurrent.TimeUnit

object LoginServer {

    private var server: HttpServer? = null

    fun createServer() {
        try {
            if (server == null) {
                server = HttpServer.create(InetSocketAddress(21851), 0)
                server?.let {
                    it.createContext("/", LoginPage())
                    it.createContext("/settoken", SetTokenPage())

                    it.start()
                    Multithreading.schedule({
                        if (server != null) {
                            it.stop(0)
                            server = null
                            UChat.chat("[Craftify]: Web Server timed out, took to long to login.")
                        }
                    }, 2, TimeUnit.MINUTES)
                }
            }
        } catch (e: Exception) {
            println(e)
        }
        val opened = try {
            browse(URI("https://accounts.spotify.com/authorize?client_id=7314a0ab3c734b2caa4b483032cdd91f&response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A21851%2F&scope=user-read-playback-state%20user-modify-playback-state"))
        } catch (ignored: Exception) {
            false
        }
        if (!opened) UChat.chat("Please open this link in your browser: https://accounts.spotify.com/authorize?client_id=7314a0ab3c734b2caa4b483032cdd91f&response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A21851%2F&scope=user-read-playback-state%20user-modify-playback-state")
    }

    fun destoryServer() {
        server = null
    }
}
