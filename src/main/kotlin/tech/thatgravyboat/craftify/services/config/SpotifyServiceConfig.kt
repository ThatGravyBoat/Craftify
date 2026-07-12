package tech.thatgravyboat.craftify.services.config

import com.google.gson.JsonObject
import tech.thatgravyboat.craftify.server.LoginServer
import tech.thatgravyboat.craftify.utils.Utils
import tech.thatgravyboat.craftify.utils.getString

object SpotifyServiceConfig : ServiceConfig {

    var auth: String = ""
    var refresh: String = ""

    override fun load(data: JsonObject) {
        this.auth = data.getString("auth") ?: auth
        this.refresh = data.getString("refresh") ?: refresh
    }

    override fun save(data: JsonObject) {
        data.addProperty("auth", this.auth)
        data.addProperty("refresh", this.refresh)
    }

    override fun getEntries() = buildList {
        button("Login", "Click to log in if you haven't already. This will open a web browser where you will have 120s to accept and login.") {
            LoginServer.createServer()
            Utils.openUrl("https://craftify.thatgravyboat.tech/login")
        }
        divider("Developer Options", "These options are for advanced users only. If you don't know what they do, don't touch them.")
        password("Authentication Token", "The token used to access spotify.", ::auth)
        password("Refresh Token", "The token used to refresh the login token.", ::refresh)
    }
}