package tech.thatgravyboat.craftify

import gg.essential.api.utils.GuiUtil
import gg.essential.universal.UDesktop
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import tech.thatgravyboat.craftify.server.LoginServer
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.ui.Player
import tech.thatgravyboat.craftify.ui.enums.Position
import java.io.File
import java.net.URI

@Suppress("unused")
object Config : Vigilant(File("./config/craftify.toml")) {

    @Property(
        type = PropertyType.SWITCH,
        name = "First Time",
        category = "General",
        description = "Determines whether to show a startup message.",
        hidden = true
    )
    var firstTime = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Enable",
        category = "General",
        description = "Enables the mod."
    )
    var enable = true

    @Property(
        type = PropertyType.SELECTOR,
        name = "Link Mode",
        options = [
            "Open In Browser",
            "Copy To Clipboard"
        ],
        category = "General",
        description = "How you will get/display the link when you click on the link button."
    )
    var linkMode = 1

    @Property(
        type = PropertyType.BUTTON,
        name = "Theme Config",
        category = "General",
        placeholder = "Open",
        description = "Open theme config."
    )
    fun themeConfig() {
        ThemeConfig.gui()?.let {
            GuiUtil.open(it)
        }
    }

    @Property(
        type = PropertyType.SELECTOR,
        options = [
            "TOP LEFT",
            "TOP MIDDLE",
            "TOP RIGHT",
            "MIDDLE LEFT",
            "MIDDLE RIGHT",
            "BOTTOM LEFT",
            "BOTTOM MIDDLE",
            "BOTTOM RIGHT"
        ],
        name = "Position",
        description = "The position at which the display will be.",
        category = "Rendering"
    )
    var position = 0

    @Property(
        type = PropertyType.SELECTOR,
        options = [
            "Escape Menu Only",
            "In Game Only",
            "Non Intrusive",
            "Always",
            "Inventory Only"
        ],
        name = "Render Type",
        description = "How/When the song with display.",
        category = "Rendering"
    )
    var renderType = 2

    @Property(
        type = PropertyType.SELECTOR,
        options = [
            "All The Time",
            "When Playing",
            "When Song Found"
        ],
        name = "Display Mode",
        description = "When it will display.",
        category = "Rendering"
    )
    var displayMode = 2

    @Property(
        type = PropertyType.SWITCH,
        name = "Controls",
        category = "Rendering",
        description = "Will allow you to pause/play, skip forward and backwards, repeat, and shuffle the music in game. (Requires Spotify Premium)"
    )
    var premiumControl = false

    @Property(
        type = PropertyType.SELECTOR,
        options = [
            "Disable",
            "Chat Message",
            "Essential Notification"
        ],
        name = "Announce New Song",
        category = "General",
        description = "Send a notifcation when a new song is playing."
    )
    var announceNewSong = 0

    @Property(
        type = PropertyType.SELECTOR,
        options = [
            "None",
            "Left",
            "Right"
        ],
        name = "Notification Album Art Location",
        description = "Where should the album art be located when Announce New Song is set to Essential Notifcation.",
        category = "General"
    )
    var announcementRendering = 1

    @Property(
        type = PropertyType.BUTTON,
        name = "Login Button",
        description = "Click to login in if you haven't already. This will open a web browser where you will have 120s to accept and login.",
        placeholder = "Login",
        category = "Login"
    )
    fun login() {
        LoginServer.createServer()
    }

    @Property(
        type = PropertyType.TEXT,
        protectedText = true,
        name = "Login Token",
        description = "The token to access spotify. You should never need to manually edit this.",
        category = "Login"
    )
    var token = ""

    @Property(
        type = PropertyType.TEXT,
        protectedText = true,
        name = "Refresh Token",
        description = "The token to reaccess spotify.",
        category = "Login",
        hidden = true
    )
    var refreshToken = ""

    @Property(type = PropertyType.BUTTON, "Discord", category = "General", subcategory = "Self Promotion", placeholder = "Visit")
    fun discord() {
        UDesktop.browse(URI("https://discord.gg/jRhkYFmpCa"))
    }

    @Property(type = PropertyType.BUTTON, "Patreon", category = "General", subcategory = "Self Promotion", placeholder = "Visit")
    fun patreon() {
        UDesktop.browse(URI("https://patreon.com/thatgravyboat"))
    }

    @Property(type = PropertyType.BUTTON, "Twitter", category = "General", subcategory = "Self Promotion", placeholder = "Visit")
    fun twitter() {
        UDesktop.browse(URI("https://twitter.com/ThatGravyBoat"))
    }

    @Property(type = PropertyType.BUTTON, "YouTube", category = "General", subcategory = "Self Promotion", placeholder = "Visit")
    fun rickroll() {
        UDesktop.browse(URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"))
    }

    init {
        initialize()

        registerListener("position") { it: Int ->
            Player.changePosition(Position.values()[it])
        }
    }

    fun hasToken() = token.isNotBlank()

    fun optionalRefresh(): String? = refreshToken.ifBlank { null }
}
