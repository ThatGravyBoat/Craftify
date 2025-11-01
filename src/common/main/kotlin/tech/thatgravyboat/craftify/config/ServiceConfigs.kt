package tech.thatgravyboat.craftify.config

import gg.essential.vigilance.data.*
import tech.thatgravyboat.craftify.server.LoginServer
import tech.thatgravyboat.craftify.services.config.BeefwebServiceConfig
import tech.thatgravyboat.craftify.services.config.MpdServiceConfig
import tech.thatgravyboat.craftify.services.config.SpotifyServiceConfig
import tech.thatgravyboat.craftify.services.config.YoutubeServiceConfig
import tech.thatgravyboat.craftify.utils.Utils
import kotlin.reflect.KMutableProperty0

object ServiceConfigs {

    val SPOTIFY = listOf(
        Button("Login", "Click to log in if you haven't already. This will open a web browser where you will have 120s to accept and login.") {
            LoginServer.createServer()
            Utils.openUrl("https://craftify.thatgravyboat.tech/login")
        },
        DividerItem(
            "Developer Options",
            "These options are for advanced users only. If you don't know what they do, don't touch them."
        ),
        Entry(
            SpotifyServiceConfig::auth,
            type = PropertyType.TEXT,
            name = "Spotify Login Token",
            description = "The token to access spotify.",
            protected = true,
        ),
        Entry(
            SpotifyServiceConfig::refresh,
            type = PropertyType.TEXT,
            name = "Spotify Refresh Token",
            description = "The token to refresh login token.",
            protected = true,
        ),
    )

    val YOUTUBE = listOf(
        Button("Login", "Click to log in if you haven't already. This will open a web browser where you will have 120s to accept and login.") {
            LoginServer.createServer()
            Utils.openUrl("http://localhost:21851/youtube")
        },
        DividerItem(
            "Developer Options",
            "These options are for advanced users only. If you don't know what they do, don't touch them."
        ),
        Entry(
            YoutubeServiceConfig::token,
            type = PropertyType.TEXT,
            name = "Login Token",
            description = "The token to access YTMD.",
            protected = true,
        ),
    )

    val CIDER = listOf(
        DividerItem(
            "Notice",
            "For cider to work you need to enable websocket api in Cider settings."
        ),
    )

    val CIDER2 = listOf(
        DividerItem(
            "Notice",
            "For Cider 2 to work you need to go to connectivity in Cider settings and enable the WebSockets API and RPC Server."
        ),
    )

    val APPLESCRIPT = listOf(
        DividerItem(
            "Notice",
            "This service only works on MacOS and requires no configuration, but it will use AppleScript to control the Apple Music app."
        ),
    )

    val BEEFWEB = listOf(
        Entry(
            BeefwebServiceConfig::port,
            type = PropertyType.NUMBER,
            name = "Service Port",
            description = "The port to use to connect to the service.",
            min = 0, max = 65535,
        )
    )

    val MPD = listOf(
        Entry(
            MpdServiceConfig::port,
            type = PropertyType.NUMBER,
            name = "Service Port",
            description = "The port to use to connect to the service.",
            min = 0, max = 65535,
        )
    )

    val TIDAL = listOf(
        DividerItem(
            "Notice",
            "For Tidal to work you need both web api and playback controls turned on it the app settings."
        ),
    )

    private fun Button(name: String, description: String, function: () -> Unit): PropertyItem = PropertyItem(
        PropertyData(
            PropertyAttributesExt(type = PropertyType.BUTTON, name = name, category = "General", description = description),
            KFunctionBackedPropertyValue(function),
            Config
        ),
        ""
    )

    private inline fun <reified T> Entry(
        property: KMutableProperty0<T>,
        type: PropertyType,
        name: String,
        description: String,
        min: Int = 0,
        max: Int = 0,
        protected: Boolean = false,
    ): PropertyItem = PropertyItem(
        PropertyData(
            PropertyAttributesExt(type = type, name = name, category = "General", description = description, min = min, max = max, protected = protected),
            ReadWritePropertyValue.create(property),
            Config
        ),
        ""
    )
}