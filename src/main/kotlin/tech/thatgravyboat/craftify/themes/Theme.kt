package tech.thatgravyboat.craftify.themes

import com.google.gson.GsonBuilder
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
        ThemeConfig.markDirty()
        ThemeConfig.writeData()
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
                ThemeConfig.selectedHoverControlColor
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
