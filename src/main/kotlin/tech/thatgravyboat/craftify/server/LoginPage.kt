package tech.thatgravyboat.craftify.server

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler

class LoginPage(private val file: String) : HttpHandler {

    override fun handle(exchange: HttpExchange?) {
        val loginPage = this.javaClass.classLoader.getResourceAsStream("${file}.html")
        val data: ByteArray
        val length: Long
        if (loginPage == null) {
            data = "Login Page not found in jar resources. Report to ThatGravyBoat#0001 on discord!".toByteArray()
            length = data.size.toLong()
        } else {
            data = ByteArray(loginPage.available())
            length = loginPage.read(data).toLong()
            loginPage.close()
        }
        exchange?.let {
            it.sendResponseHeaders(200, length)
            it.responseBody?.write(data)
            exchange.close()
        }
    }
}
