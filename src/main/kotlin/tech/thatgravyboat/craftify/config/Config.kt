package tech.thatgravyboat.craftify.config

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.ui.Player
import tech.thatgravyboat.craftify.ui.enums.Anchor
import tech.thatgravyboat.craftify.utils.Utils
import java.io.File

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
            "YT Music Desktop App",
            "Cider (Apple Music)",
            "Beefweb (Foobar2000 & DeaDBeeF)",
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
            Utils.openScreen(it)
        }
    }

    @Property(
        hidden = true,
        type = PropertyType.SWITCH,
        name = "Send the Essential packets",
        category = "General",
        description = "Do not turn on unless told otherwise by ThatGravyBoat"
    )
    var thisIsForTestingEssentialPacketsDoNotTurnOn = false

    @Property(
        hidden = true,
        type = PropertyType.SWITCH,
        name = "Send the packets",
        category = "General",
        description = "Do not turn on unless told otherwise by ThatGravyBoat"
    )
    var thisIsForTestingPacketsDoNotTurnOn = false

    @Property(
        hidden = true,
        type = PropertyType.SWITCH,
        name = "Send Songs To Servers",
        category = "General",
        description = "Send the song you're listening to the server you are currently on when it changes."
    )
    var sendPackets = false

    @Property(
        hidden = true,
        type = PropertyType.PARAGRAPH,
        name = "Allowed Servers",
        category = "General",
        description = "The servers you are allowed to send packets to. (Separate with newlines)\n§5Note: If left blank all servers will be allowed.\n§5Will allow all LAN servers if packets enabled."
    )
    var allowedServers = "your.server.here"

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
            "Inventory Only",
            "Esc. and Inv. Menu",
            "Tab Overlay"
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
        description = "Send a notification when a new song is playing."
    )
    var announceNewSong = 0

    @Property(
            type = PropertyType.TEXT,
            name = "Announcement Message",
            category = "General",
            description = "Format for the announcement message (chat message). \${song} will be replaced with the songname and \${artists} will be replaced by the artists"
    )
    var announcementMessage = "&aCraftify > &7Now Playing: &b\${song} by \${artists}"

    @Property(
        type = PropertyType.SELECTOR,
        options = [
            "None",
            "Left",
            "Right"
        ],
        name = "Notification Album Art Location",
        description = "Where should the album art be located when Announce New Song is set to Essential Notification.",
        category = "General"
    )
    var announcementRendering = 1

    @Property(
        type = PropertyType.CUSTOM,
        name = "Login Button",
        description = "You change to choose a Mod Mode first in General. Click to login in if you haven't already. This will open a web browser where you will have 120s to accept and login.\n&cNote: ONLY FOR SPOTIFY! On YT this just refreshes the session.",
        placeholder = "Login",
        category = "Login",
        customPropertyInfo = LoginButtonProperty::class
    )
    var login: Any? = "login"

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
        type = PropertyType.NUMBER,
        min = 0,
        max = 65535,
        name = "Service Port",
        description = "The port to use for certain services. Only for Mod Mode: Beefweb",
        category = "Login"
    )
    var servicePort = 8880

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
        Utils.openUrl("https://discord.gg/jRhkYFmpCa")
    }

    @Property(type = PropertyType.BUTTON, "Patreon", category = "General", subcategory = "Self Promotion", placeholder = "Visit")
    fun patreon() {
        Utils.openUrl("https://patreon.com/thatgravyboat")
    }

    @Property(type = PropertyType.BUTTON, "Twitter", category = "General", subcategory = "Self Promotion", placeholder = "Visit")
    fun twitter() {
        Utils.openUrl("https://twitter.com/ThatGravyBoat")
    }

    @Property(type = PropertyType.BUTTON, "YouTube", category = "General", subcategory = "Self Promotion", placeholder = "Visit")
    fun rickroll() {
        Utils.openUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
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
