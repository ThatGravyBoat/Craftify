package tech.thatgravyboat.craftify.services.config

import com.google.gson.JsonObject
import tech.thatgravyboat.craftify.utils.getInt

object BeefwebServiceConfig : ServiceConfig {

    var port: Int = 8880

    override fun load(data: JsonObject) {
        this.port
        this.port = data.getInt("port") ?: port
    }

    override fun save(data: JsonObject) {
        data.addProperty("port", this.port)
    }

    override fun getEntries() = buildList {
        number("Service Port", "The port used to connect to beefweb.", ::port)
    }
}