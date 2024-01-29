package tech.thatgravyboat.craftify.themes

import gg.essential.universal.UDesktop
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyData
import gg.essential.vigilance.data.PropertyType
import gg.essential.vigilance.data.SortingBehavior
import tech.thatgravyboat.craftify.ui.Player
import tech.thatgravyboat.craftify.utils.Utils
import java.awt.Color
import java.io.File

@Suppress("unused")
object ThemeConfig : Vigilant(
    File("./config/craftify-theme.toml"),
    sortingBehavior = object : SortingBehavior() {
        override fun getSubcategoryComparator(): Comparator<in Map.Entry<String, List<PropertyData>>> {
            return Comparator { o1: Map.Entry<String, List<PropertyData>>, o2: Map.Entry<String, List<PropertyData>> ->
                if (o1.key == o2.key) return@Comparator 0
                return@Comparator if (o1.key == "Buttons") -1 else 1
            }
        }
    }
) {

    @Property(
        type = PropertyType.BUTTON,
        name = "Copy To Clipboard",
        placeholder = "Copy",
        subcategory = "Buttons",
        category = "Theme"
    )
    fun copy() {
        UDesktop.setClipboardString(Theme.fromConfig().getData())
    }

    @Property(
        type = PropertyType.BUTTON,
        name = "Set config from clipboard.",
        placeholder = "Load",
        subcategory = "Buttons",
        category = "Theme"
    )
    fun load() {
        try {
            Theme.fromJson(UDesktop.getClipboardString()).setConfig()
        } catch (ignored: Exception) {
            // Don't do anything if fails.
        }
        ThemeConfig.gui()?.let { Utils.openScreen(it) }
    }

    @Property(
        type = PropertyType.BUTTON,
        name = "Reset config.",
        placeholder = "Reset",
        subcategory = "Buttons",
        category = "Theme"
    )
    fun reset() {
        Theme().setConfig()
        ThemeConfig.gui()?.let { Utils.openScreen(it) }
    }

    @Property(
        type = PropertyType.SWITCH,
        name = "Hide Image",
        category = "Theme"
    )
    var hideImage = false

    @Property(
        type = PropertyType.COLOR,
        name = "Border Color",
        category = "Theme"
    )
    var borderColor: Color = Color(1, 165, 82)

    @Property(
        type = PropertyType.COLOR,
        name = "Title Color",
        category = "Theme"
    )
    var titleColor: Color = Color.WHITE

    @Property(
        type = PropertyType.SELECTOR,
        name = "Title Font",
        category = "Theme",
        options = [
            "Vanilla",
            "Minecraft Vector",
            "Minecraft Vector Bold",
            "JetBrains Mono",
            "Minecraft Five"
        ]
    )
    var titleFont: Int = 0

    @Property(
        type = PropertyType.COLOR,
        name = "Artist Color",
        category = "Theme"
    )
    var artistColor: Color = Color.WHITE

    @Property(
        type = PropertyType.SELECTOR,
        name = "Artist Font",
        category = "Theme",
        options = [
            "Vanilla",
            "Minecraft Vector",
            "Minecraft Vector Bold",
            "JetBrains Mono",
            "Minecraft Five"
        ]
    )
    var artistFont: Int = 0

    @Property(
        type = PropertyType.COLOR,
        name = "Background Color",
        category = "Theme"
    )
    var backgroundColor: Color = Color(0, 0, 0, 80)

    @Property(
        type = PropertyType.COLOR,
        name = "Background Color",
        category = "Theme",
        subcategory = "Progress Bar"
    )
    var progressBackgroundColor: Color = Color(50, 50, 50)

    @Property(
        type = PropertyType.COLOR,
        name = "Color",
        category = "Theme",
        subcategory = "Progress Bar"
    )
    var progressColor: Color = Color.WHITE

    @Property(
        type = PropertyType.COLOR,
        name = "Number Color",
        category = "Theme",
        subcategory = "Progress Bar"
    )
    var progressNumberColor: Color = Color.WHITE

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Radius",
        category = "Theme",
        subcategory = "Progress Bar",
        minF = 0f,
        maxF = 10f,
        decimalPlaces = 0
    )
    var progressRadius: Float = 3f

    @Property(
        type = PropertyType.SELECTOR,
        name = "Font",
        category = "Theme",
        subcategory = "Progress Bar",
        options = [
            "Vanilla",
            "Minecraft Vector",
            "Minecraft Vector Bold",
            "JetBrains Mono",
            "Minecraft Five"
        ]
    )
    var progressFont: Int = 0

    @Property(
        type = PropertyType.COLOR,
        name = "Color",
        category = "Theme",
        subcategory = "Controls"
    )
    var controlColor: Color = Color.WHITE

    @Property(
        type = PropertyType.COLOR,
        name = "Hover Color",
        category = "Theme",
        subcategory = "Controls"
    )
    var hoverControlColor: Color = Color(150, 150, 150)

    @Property(
        type = PropertyType.COLOR,
        name = "Selected Color",
        category = "Theme",
        subcategory = "Controls"
    )
    var selectedControlColor: Color = Color(1, 165, 82)

    @Property(
        type = PropertyType.COLOR,
        name = "Selected Hover Color",
        category = "Theme",
        subcategory = "Controls"
    )
    var selectedHoverControlColor: Color = Color(0, 212, 105)

    @Property(
        type = PropertyType.TEXT,
        name = "Settings",
        category = "Theme",
        subcategory = "Icons"
    )
    var settingsIcon: String = "https://i.imgur.com/Nd4gQzY.png"

    @Property(
            type = PropertyType.SWITCH,
            name = "Show settings button",
            category = "Theme",
            subcategory = "Icons"
    )
    var showSettingsButton: Boolean = true

    @Property(
        type = PropertyType.TEXT,
        name = "Shuffle",
        category = "Theme",
        subcategory = "Icons"
    )
    var shuffleIcon: String = "https://i.imgur.com/W58UJGf.png"

    @Property(
            type = PropertyType.SWITCH,
            name = "Show shuffle button",
            category = "Theme",
            subcategory = "Icons"
    )
    var showShuffleButton: Boolean = true

    @Property(
        type = PropertyType.TEXT,
        name = "Previous",
        category = "Theme",
        subcategory = "Icons"
    )
    var previousIcon: String = "https://i.imgur.com/Lb4YYOu.png"

    @Property(
            type = PropertyType.SWITCH,
            name = "Show previous button",
            category = "Theme",
            subcategory = "Icons"
    )
    var showPreviousButton: Boolean = true

    @Property(
        type = PropertyType.TEXT,
        name = "Pause",
        category = "Theme",
        subcategory = "Icons"
    )
    var pauseIcon: String = "https://i.imgur.com/JQdBt2K.png"

    @Property(
        type = PropertyType.TEXT,
        name = "Play",
        category = "Theme",
        subcategory = "Icons"
    )
    var playIcon: String = "https://i.imgur.com/9tsZMcO.png"

    @Property(
            type = PropertyType.SWITCH,
            name = "Show play/pause toggle",
            category = "Theme",
            subcategory = "Icons"
    )
    var showPlayButton: Boolean = true

    @Property(
        type = PropertyType.TEXT,
        name = "Next",
        category = "Theme",
        subcategory = "Icons"
    )
    var nextIcon: String = "https://i.imgur.com/4L2322Q.png"

    @Property(
            type = PropertyType.SWITCH,
            name = "Show next button",
            category = "Theme",
            subcategory = "Icons"
    )
    var showNextButton: Boolean = true

    @Property(
        type = PropertyType.TEXT,
        name = "Repeat",
        category = "Theme",
        subcategory = "Icons"
    )
    var repeatIcon: String = "https://i.imgur.com/C8h1RBc.png"

    @Property(
            type = PropertyType.SWITCH,
            name = "Show repeat button",
            category = "Theme",
            subcategory = "Icons"
    )
    var showRepeatButton: Boolean = true

    @Property(
        type = PropertyType.TEXT,
        name = "External Link",
        category = "Theme",
        subcategory = "Icons"
    )
    var externalIcon: String = "https://i.imgur.com/qQs0WHt.png"

    @Property(
            type = PropertyType.SWITCH,
            name = "Show external link button",
            category = "Theme",
            subcategory = "Icons"
    )
    var showExternalButton: Boolean = true

    @Property(
        type = PropertyType.TEXT,
        name = "Volume Control",
        category = "Theme",
        subcategory = "Icons"
    )
    var volumeIcon: String = "https://i.imgur.com/RNfbruf.png"

    @Property(
            type = PropertyType.SWITCH,
            name = "Show volume control button",
            category = "Theme",
            subcategory = "Icons"
    )
    var showVolumeButton: Boolean = true

    @Property(
        type = PropertyType.TEXT,
        name = "Position Editor",
        category = "Theme",
        subcategory = "Icons"
    )
    var positionEditorIcon: String = "https://i.imgur.com/XZWUSSe.png"

    @Property(
            type = PropertyType.SWITCH,
            name = "Show position editor button",
            category = "Theme",
            subcategory = "Icons"
    )
    var showPositionEditorButton: Boolean = true

    init {
        initialize()
        registerListener("progressRadius") { _: Float -> Player.updateTheme() }
    }
}
