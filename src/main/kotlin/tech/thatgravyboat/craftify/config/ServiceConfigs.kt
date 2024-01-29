package tech.thatgravyboat.craftify.config

import gg.essential.vigilance.data.*
import tech.thatgravyboat.craftify.server.LoginServer
import tech.thatgravyboat.craftify.utils.Utils

object ServiceConfigs {

    private val SPOTIFY = listOf(
        PropertyItem(
            PropertyData(
                PropertyAttributesExt(
                    type = PropertyType.BUTTON,
                    name = "Login",
                    category = "General",
                    description = "Click to log in if you haven't already. This will open a web browser where you will have 120s to accept and login.",
                ),
                KFunctionBackedPropertyValue {
                    LoginServer.createServer()
                    Utils.openUrl("https://craftify.thatgravyboat.tech/login")
                },
                Config
            ),
            ""
        ),
        DividerItem(
            "Developer Options",
            "These options are for advanced users only. If you don't know what they do, don't touch them."
        ),
        PropertyItem(
            PropertyData(
                PropertyAttributesExt(
                    type = PropertyType.TEXT,
                    name = "Spotify Login Token",
                    category = "General",
                    description = "The token to access spotify.",
                    protected = true,
                ),
                ReadWritePropertyValue(
                    { Config.token },
                    {
                        if (it is String) {
                            Config.token = it
                        } else {
                            val name = it?.let { it::class.simpleName } ?: "null"
                            println("Invalid token type: $name")
                        }
                    }
                ),
                Config
            ),
            ""
        ),
        PropertyItem(
            PropertyData(
                PropertyAttributesExt(
                    type = PropertyType.TEXT,
                    name = "Spotify Refresh Token",
                    category = "General",
                    description = "The token to refresh login token.",
                    protected = true,
                ),
                ReadWritePropertyValue(
                    { Config.refreshToken },
                    {
                        if (it is String) {
                            Config.refreshToken = it
                        } else {
                            val name = it?.let { it::class.simpleName } ?: "null"
                            println("Invalid refresh token type: $name")
                        }
                    }
                ),
                Config
            ),
            ""
        ),
    )

    private val YOUTUBE = listOf(
        PropertyItem(
            PropertyData(
                PropertyAttributesExt(
                    type = PropertyType.BUTTON,
                    name = "Login",
                    category = "General",
                    description = "Click to log in if you haven't already. This will open a web browser where you will have 120s to accept and login.",
                ),
                KFunctionBackedPropertyValue {
                    LoginServer.createServer()
                    Utils.openUrl("http://localhost:21851/youtube")
                },
                Config
            ),
            ""
        ),
        DividerItem(
            "Developer Options",
            "These options are for advanced users only. If you don't know what they do, don't touch them."
        ),
        PropertyItem(
            PropertyData(
                PropertyAttributesExt(
                    type = PropertyType.TEXT,
                    name = "Login Token",
                    category = "General",
                    description = "The token to access YTMD.",
                    protected = true,
                ),
                ReadWritePropertyValue(
                    { Config.ytmdToken },
                    {
                        if (it is String) {
                            Config.ytmdToken = it
                        } else {
                            val name = it?.let { it::class.simpleName } ?: "null"
                            println("Invalid token type: $name")
                        }
                    }
                ),
                Config
            ),
            ""
        ),
    )

    private val CIDER = listOf(
        DividerItem(
            "Notice",
            "For cider to work you need to enable websocket api in Cider settings."
        ),
    )

    private val BEEFWEB = listOf(
        PropertyItem(
            PropertyData(
                PropertyAttributesExt(
                    type = PropertyType.NUMBER,
                    name = "Service Port",
                    category = "General",
                    description = "The port to use to connect to the service.",
                    min = 0, max = 65535,
                ),
                ReadWritePropertyValue(
                    { Config.servicePort },
                    {
                        if (it is Int) {
                            Config.servicePort = it
                        } else {
                            val name = it?.let { it::class.simpleName } ?: "null"
                            println("Invalid token type: $name")
                        }
                    }
                ),
                Config
            ),
            ""
        ),
    )

    fun get(service: String?): List<CategoryItem> {
        return when (service) {
            "spotify" -> SPOTIFY
            "ytmd" -> YOUTUBE
            "cider" -> CIDER
            "beefweb" -> BEEFWEB
            else -> listOf()
        }
    }
}