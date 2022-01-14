package tech.thatgravyboat.craftify.ui

import gg.essential.api.utils.Multithreading
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.SVGComponent
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.svg.data.SVG
import tech.thatgravyboat.craftify.themes.ThemeConfig

class UIButton(private val original: SVG, private val clicked: SVG, private val color: Boolean = false, click: (UIComponent.(state: Boolean) -> Unit)? = null) : UIContainer() {

    private var state = false

    private val icon = SVGComponent(original).constrain {
        height = 100.percent()
        width = 100.percent()
    } childOf this

    init {
        onMouseClick { event ->
            if (event.mouseButton == 0) {
                updateStateWithHover(!state)
                if (click != null) {
                    Multithreading.runAsync { click(this, state) }
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
            icon.setSVG(if (state) clicked else original)
            icon.setColor(if (state && color) ThemeConfig.selectedControlColor else ThemeConfig.controlColor)
        }
    }

    private fun updateStateWithHover(state: Boolean) {
        this.state = state
        icon.setSVG(if (state) clicked else original)
        icon.setColor(
            if (state && color)
                (if (this.isHovered()) ThemeConfig.selectedHoverControlColor else ThemeConfig.selectedControlColor)
            else
                (if (this.isHovered()) ThemeConfig.hoverControlColor else ThemeConfig.controlColor)
        )
    }
}
