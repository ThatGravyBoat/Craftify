package tech.thatgravyboat.craftify.config

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import tech.thatgravyboat.craftify.screens.servers.ServersScreen
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.ui.Player
import tech.thatgravyboat.craftify.ui.enums.Anchor
import tech.thatgravyboat.craftify.utils.Utils
import java.io.File

object Config : Vigilant(File("./config/craftify.toml")) {

    @Property(
        type = PropertyType.CUSTOM,
        name = "Music Service",
        description = "What service you want to use for fetching the music you're listening to?",
        category = "General",
        customPropertyInfo = ServiceProperty::class
    )
    var musicService: String? = "disabled"

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
        type = PropertyType.SWITCH,
        name = "Send Songs To Servers",
        category = "Servers",
        description = "Send the song you're listening to the allowed server you are currently on when it changes."
    )
    var sendPackets = false

    @Property(hidden = true, type = PropertyType.TEXT, name = "Allowed Servers", category = "Servers")
    var allowedServers = ""

    @Property(
        type = PropertyType.BUTTON,
        "Shared Servers",
        category = "Servers",
        placeholder = "Open",
        description = "The server that you allow automatic sharing of your current song to.\nThis allows other mods to get your current song to display it, should not be turned on unless you know a mod uses it."
    )
    fun servers() {
        Utils.openScreen(ServersScreen())
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
            "Essential Notification (<=1.12)"
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
        description = """
Format for the announcement message (chat message). 
Variables:
- ${'$'}{song} will be replaced with the song name.
- ${'$'}{artists} will be replaced by the artists.
- ${'$'}{artist} will be replaced by the first artist.
"""
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

    //region Service Settings
    @Property(type = PropertyType.NUMBER, name = "Service Port", category = "Login", hidden = true)
    var servicePort = 8880

    @Property(type = PropertyType.SWITCH, name = "First Time", category = "General", hidden = true)
    var firstTime = true

    @Property(type = PropertyType.TEXT, name = "Spotify Login Token", category = "Login", hidden = true)
    var token = ""

    @Property(type = PropertyType.TEXT, name = "Refresh Token", category = "Login", hidden = true)
    var refreshToken = ""

    @Property(type = PropertyType.TEXT, name = "ytmd Token", category = "Login", hidden = true)
    var ytmdToken = ""
    //endregion

    @Deprecated("Replaced with service")
    @Property(type = PropertyType.NUMBER, name = "Mod Mode", category = "General", hidden = true)
    var modMode = 0

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

    fun optionalRefresh(): String? = refreshToken.ifBlank { null }

    @Suppress("DEPRECATION")
    fun getService(): String {
        if (modMode != -1) {
            musicService = when (modMode) {
                1 -> "spotify"
                2 -> "ytmd"
                3 -> "cider"
                4 -> "beefweb"
                else -> "disabled"
            }
            modMode = -1
            markDirty()
            writeData()
        }
        return musicService ?: "disabled"
    }
}
