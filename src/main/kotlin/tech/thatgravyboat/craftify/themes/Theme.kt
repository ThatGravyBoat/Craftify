package tech.thatgravyboat.craftify.themes

import com.google.gson.GsonBuilder
import tech.thatgravyboat.craftify.ui.Player
import java.awt.Color

private val GSON = GsonBuilder()
    .registerTypeAdapter(Color::class.java, ColorSerializer)
    .create()

data class Theme(
    val borderColor: Color = Color(1, 165, 82),
    val progressBar: ProgressBar = ProgressBar(),
    val titleColor: Color = Color.WHITE,
    val titleFont: Int = 0,
    val artistColor: Color = Color.WHITE,
    val artistFont: Int = 0,
    val backgroundColor: Color = Color(0, 0, 0, 80),
    val backgroundRadius: Float = 0f,

    val controlColor: Color = Color.WHITE,
    val controlHoverColor: Color = Color(150, 150, 150),
    val selectedControlColor: Color = Color(1, 165, 82),
    val selectedControlHoverColor: Color = Color(0, 212, 105),

    val settingIcon: String = "https://files.teamresourceful.com/r/9DzwrP.png",
    val shuffleIcon: String = "https://files.teamresourceful.com/r/Bl9ZTS.png",
    val previousIcon: String = "https://files.teamresourceful.com/r/Dr7nH1.png",
    val pauseIcon: String = "https://files.teamresourceful.com/r/55kubx.png",
    val playIcon: String = "https://files.teamresourceful.com/r/rlQyJs.png",
    val nextIcon: String = "https://files.teamresourceful.com/r/FDIAxt.png",
    val repeatIcon: String = "https://files.teamresourceful.com/r/4gVqmu.png",
    val externalIcon: String = "https://files.teamresourceful.com/r/7gJ4OY.png",
    val volumeIcon: String = "https://files.teamresourceful.com/r/i7XLC1.png",
    val positionEditorIcon: String = "https://files.teamresourceful.com/r/N3c8xm.png",
) {
    fun getData(): String = GSON.toJson(this)

    fun setConfig() {
        ThemeConfig.artistColor = this.artistColor
        ThemeConfig.artistFont = this.artistFont
        ThemeConfig.titleColor = this.titleColor
        ThemeConfig.titleFont = this.titleFont
        ThemeConfig.backgroundColor = this.backgroundColor
        ThemeConfig.backgroundRadius = this.backgroundRadius
        ThemeConfig.borderColor = this.borderColor
        ThemeConfig.progressColor = this.progressBar.barColor
        ThemeConfig.progressBackgroundColor = this.progressBar.barBackgroundColor
        ThemeConfig.progressRadius = this.progressBar.barRadius
        ThemeConfig.progressNumberColor = this.progressBar.numberColor
        ThemeConfig.progressFont = this.progressBar.font
        ThemeConfig.controlColor = this.controlColor
        ThemeConfig.hoverControlColor = this.controlHoverColor
        ThemeConfig.selectedControlColor = this.selectedControlColor
        ThemeConfig.selectedHoverControlColor = this.selectedControlHoverColor
        ThemeConfig.settingsIcon = this.settingIcon
        ThemeConfig.shuffleIcon = this.shuffleIcon
        ThemeConfig.repeatIcon = this.repeatIcon
        ThemeConfig.playIcon = this.playIcon
        ThemeConfig.pauseIcon = this.pauseIcon
        ThemeConfig.previousIcon = this.previousIcon
        ThemeConfig.nextIcon = this.nextIcon
        ThemeConfig.externalIcon = this.externalIcon
        ThemeConfig.volumeIcon = this.volumeIcon
        ThemeConfig.positionEditorIcon = this.positionEditorIcon
        ThemeConfig.markDirty()
        ThemeConfig.writeData()
        Player.updateTheme()
    }

    companion object {
        fun fromJson(data: String): Theme {
            return GSON.fromJson(data, Theme::class.java)
        }

        fun fromConfig(): Theme {
            return Theme(
                ThemeConfig.borderColor,
                ProgressBar(
                    ThemeConfig.progressColor,
                    ThemeConfig.progressBackgroundColor,
                    ThemeConfig.progressRadius,
                    ThemeConfig.progressNumberColor,
                    ThemeConfig.progressFont
                ),
                ThemeConfig.titleColor,
                ThemeConfig.titleFont,
                ThemeConfig.artistColor,
                ThemeConfig.artistFont,
                ThemeConfig.backgroundColor,
                ThemeConfig.backgroundRadius,
                ThemeConfig.controlColor,
                ThemeConfig.hoverControlColor,
                ThemeConfig.selectedControlColor,
                ThemeConfig.selectedHoverControlColor,
                ThemeConfig.settingsIcon,
                ThemeConfig.shuffleIcon,
                ThemeConfig.previousIcon,
                ThemeConfig.pauseIcon,
                ThemeConfig.playIcon,
                ThemeConfig.nextIcon,
                ThemeConfig.repeatIcon,
                ThemeConfig.externalIcon,
                ThemeConfig.volumeIcon,
                ThemeConfig.positionEditorIcon,
            )
        }
    }
}

data class ProgressBar(
    val barColor: Color = Color(255, 255, 255),
    val barBackgroundColor: Color = Color(50, 50, 50),
    val barRadius: Float = 3f,
    val numberColor: Color = Color(255, 255, 255),
    val font: Int = 0,
)
