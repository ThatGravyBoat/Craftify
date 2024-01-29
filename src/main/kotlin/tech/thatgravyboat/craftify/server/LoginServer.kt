package tech.thatgravyboat.craftify.server

import com.sun.net.httpserver.HttpServer
import gg.essential.universal.UChat
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.services.ServiceHelper
import tech.thatgravyboat.craftify.utils.Utils
import java.net.InetSocketAddress
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

object LoginServer {

    private var server: HttpServer? = null
    private var timeOut: ScheduledFuture<*>? = null

    fun createServer() {
        try {
            if (server == null) {
                server = HttpServer.create(InetSocketAddress(21851), 0)
                server?.let {
                    it.createContext("/", Page("craftify/login"))
                    it.createContext("/youtube", Page("craftify/youtube"))
                    it.createContext("/spotify/token", SetTokenPage { code -> ServiceHelper.loginToSpotify("auth", code) })
                    it.createContext("/youtube/token", SetTokenPage { code ->
                        Config.ytmdToken = code
                        Config.markDirty()
                        Config.writeData()
                    })

                    it.start()
                    timeOut()
                }
            } else if (timeOut != null) {
                timeOut?.cancel(true)
                timeOut()
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    private fun timeOut() {
        timeOut = Utils.schedule(2, TimeUnit.MINUTES) {
            if (server != null) {
                server?.stop(0)
                server = null
                UChat.chat("[Craftify]: Web Server timed out, took too long to login.")
            }
        }
    }

    fun destroyServer() {
        server = null
    }
}
