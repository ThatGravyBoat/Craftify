package tech.thatgravyboat.craftify.server

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler

class SetTokenPage(private val setter: (String) -> Unit) : HttpHandler {

    override fun handle(exchange: HttpExchange?) {
        exchange?.let {
            val code = it.requestURI.query
            code?.let(setter)
            it.sendResponseHeaders(if (code == null) 400 else 200, 0)
            it.close()
            if (code == null) return
            it.httpContext.server.stop(5)
            LoginServer.destroyServer()
        }
    }
}
