package tech.thatgravyboat.craftify

import gg.essential.api.utils.GuiUtil
import gg.essential.universal.UDesktop
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import tech.thatgravyboat.craftify.server.LoginServer
import tech.thatgravyboat.craftify.services.YtmdAPI
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.ui.Player
import tech.thatgravyboat.craftify.ui.enums.Anchor
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
        type = PropertyType.SELECTOR,
        name = "Mod Mode",
        options = [
            "Disabled",
            "Spotify",
            "YT Music Desktop App"
        ],
        category = "General",
        description = "Where you would like the mod to get its information from."
    )
    var modMode = 0

    @Property(
        type = PropertyType.SELECTOR,
        name = "Link Mode",
        options = [
            "Open In Browser",
            "Copy To Clipboard",
            "Send Chat Message"
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
        name = "Anchor Point",
        description = "The Point at which the display will be anchored to.",
        category = "Rendering"
    )
    var anchorPoint = 0

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        minF = -1f,
        maxF = 1f,
        name = "Position X Offset",
        description = "The X offset",
        category = "Rendering",
        hidden = true
    )
    var xOffset = 0f

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        minF = -1f,
        maxF = 1f,
        name = "Position Y Offset",
        description = "The Y offset",
        category = "Rendering",
        hidden = true
    )
    var yOffset = 0f

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
        description = "Click to login in if you haven't already. This will open a web browser where you will have 120s to accept and login.\nONLY FOR SPOTIFY! On YT this just refreshes the session.",
        placeholder = "Login",
        category = "Login"
    )
    fun login() {
        if (modMode == 1) {
            LoginServer.createServer()
        } else if (modMode == 2) {
            YtmdAPI.restartPoller()
        }
    }

    @Property(
        type = PropertyType.TEXT,
        protectedText = true,
        name = "Spotify Login Token",
        description = "The token to access spotify. You should never need to manually edit this.",
        category = "Login"
    )
    var token = ""

    @Property(
        type = PropertyType.TEXT,
        protectedText = true,
        name = "YTMD Password",
        description = "The YTMD Password. Only for Mod Mode: YTMD",
        category = "Login"
    )
    var ytmdPassword = ""

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

        registerListener("anchorPoint") { it: Int ->
            val anchor = Anchor.values()[it]
            xOffset = anchor.getDefaultXOffset()
            yOffset = anchor.getDefaultYOffset()
            Config.markDirty()
            Config.writeData()
            Player.changePosition(anchor)
        }
    }

    fun hasToken() = token.isNotBlank()

    fun hasPassword() = ytmdPassword.isNotBlank()

    fun optionalRefresh(): String? = refreshToken.ifBlank { null }
}
