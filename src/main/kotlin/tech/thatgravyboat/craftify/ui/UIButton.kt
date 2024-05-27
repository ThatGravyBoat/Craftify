package tech.thatgravyboat.craftify.ui

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.utils.MemoryImageCache
import java.net.URL

class UIButton(private var original: URL, private var clicked: URL, private val color: Boolean = false, click: (UIComponent.(state: Boolean) -> Boolean)? = null) : UIContainer() {

    private var state = false

    private var icon = createButtonImageFromUrl(original)

    init {
        onMouseClick { event ->
            if (event.mouseButton == 0) {
                if (click != null && click(this, state)) {
                    updateStateWithHover(!state)
                }
            }
        }

        onMouseEnter {
            icon.setColor(if (state && color) ThemeConfig.selectedHoverControlColor else ThemeConfig.hoverControlColor)
        }

        onMouseLeave {
            icon.setColor(if (state && color) ThemeConfig.selectedControlColor else ThemeConfig.controlColor)
        }
    }

    fun updateState(state: Boolean) {
        if (this.state != state) {
            this.state = state
            updateImage(clicked, original)
            icon.setColor(if (state && color) ThemeConfig.selectedControlColor else ThemeConfig.controlColor)
        }
    }

    private fun updateStateWithHover(state: Boolean) {
        this.state = state
        updateImage(clicked, original)
        icon.setColor(
            if (state && color)
                (if (this.isHovered()) ThemeConfig.selectedHoverControlColor else ThemeConfig.selectedControlColor)
            else
                (if (this.isHovered()) ThemeConfig.hoverControlColor else ThemeConfig.controlColor)
        )
    }

    fun updateImage(icon: String) {
        val url = URL(icon)
        updateImage(url, url)
    }

    fun updateImage(click: String, og: String) {
        updateImage(URL(click), URL(og))
    }

    private fun updateImage(click: URL, og: URL) {
        clicked = click
        original = og
        try {
            this.removeChild(icon)
        } catch (e: Exception) {
            // Ignores an error that occurs when the button is removed from the screen when there are no children
            // Tried to fix by checking if children are empty but that returns false but when is removed says length is 0.
        }
        icon = createButtonImageFromUrl(if (state) clicked else original)
        this.addChild(icon)
    }

    private fun createButtonImageFromUrl(url: URL): UIImage {
        return UIImage.ofURL(url, MemoryImageCache).constrain {
            height = 100.percent()
            width = 100.percent()
        }
    }
}
