package tech.thatgravyboat.craftify.server

import com.sun.net.httpserver.HttpServer
import gg.essential.universal.UChat
import tech.thatgravyboat.craftify.utils.Utils
import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

object LoginServer {

    private var server: HttpServer? = null

    fun createServer() {
        try {
            if (server == null) {
                server = HttpServer.create(InetSocketAddress(21851), 0)
                server?.let {
                    it.createContext("/", LoginPage("login"))
                    it.createContext("/settoken", SetTokenPage())

                    it.start()
                    Utils.schedule(2, TimeUnit.MINUTES) {
                        if (server != null) {
                            it.stop(0)
                            server = null
                            UChat.chat("[Craftify]: Web Server timed out, took to long to login.")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println(e)
        }
        Utils.openUrl("https://craftify.thatgravyboat.tech/login")
    }

    fun destroyServer() {
        server = null
    }
}
