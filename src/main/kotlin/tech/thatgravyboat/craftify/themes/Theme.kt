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
    val artistColor: Color = Color.WHITE,
    val backgroundColor: Color = Color(0, 0, 0, 80),

    val controlColor: Color = Color.WHITE,
    val controlHoverColor: Color = Color(150, 150, 150),
    val selectedControlColor: Color = Color(1, 165, 82),
    val selectedControlHoverColor: Color = Color(0, 212, 105),

    val settingIcon: String = "https://i.imgur.com/Nd4gQzY.png",
    val shuffleIcon: String = "https://i.imgur.com/W58UJGf.png",
    val previousIcon: String = "https://i.imgur.com/Lb4YYOu.png",
    val pauseIcon: String = "https://i.imgur.com/9tsZMcO.png",
    val playIcon: String = "https://i.imgur.com/JQdBt2K.png",
    val nextIcon: String = "https://i.imgur.com/4L2322Q.png",
    val repeatIcon: String = "https://i.imgur.com/C8h1RBc.png",
    val externalIcon: String = "https://i.imgur.com/qQs0WHt.png",
    val volumeIcon: String = "https://i.imgur.com/RNfbruf.png",
    val positionEditorIcon: String = "https://i.imgur.com/XZWUSSe.png",
) {
    fun getData(): String = GSON.toJson(this)

    fun setConfig() {
        ThemeConfig.artistColor = this.artistColor
        ThemeConfig.titleColor = this.titleColor
        ThemeConfig.backgroundColor = this.backgroundColor
        ThemeConfig.borderColor = this.borderColor
        ThemeConfig.progressColor = this.progressBar.barColor
        ThemeConfig.progressBackgroundColor = this.progressBar.barBackgroundColor
        ThemeConfig.progressRadius = this.progressBar.barRadius
        ThemeConfig.progressNumberColor = this.progressBar.numberColor
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
                    ThemeConfig.progressNumberColor
                ),
                ThemeConfig.titleColor,
                ThemeConfig.artistColor,
                ThemeConfig.backgroundColor,
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
    val numberColor: Color = Color(255, 255, 255)
)
