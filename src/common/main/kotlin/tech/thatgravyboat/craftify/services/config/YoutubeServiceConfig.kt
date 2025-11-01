package tech.thatgravyboat.craftify.services.config

import com.google.gson.JsonObject
import tech.thatgravyboat.craftify.server.LoginServer
import tech.thatgravyboat.craftify.utils.Utils
import tech.thatgravyboat.craftify.utils.getString

object YoutubeServiceConfig : ServiceConfig {

    var token: String = ""

    override fun load(data: JsonObject) {
        this.token = data.getString("token") ?: token
    }

    override fun save(data: JsonObject) {
        data.addProperty("token", this.token)
    }

    override fun getEntries() = buildList {
        button("Login", "Click to log in if you haven't already. This will open a web browser where you will have 120s to accept and login.") {
            LoginServer.createServer()
            Utils.openUrl("http://localhost:21851/youtube")
        }
        divider("Developer Options", "These options are for advanced users only. If you don't know what they do, don't touch them.")
        password("Authentication Token", "The token used to access YTMD.", ::token)
    }
}