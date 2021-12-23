package tech.thatgravyboat.craftify.ui

import gg.essential.api.utils.Multithreading
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.SVGComponent
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.svg.data.SVG
import gg.essential.elementa.utils.withAlpha
import gg.essential.vigilance.gui.VigilancePalette
import java.awt.Color

private val BUTTON_HOVER = Color(0, 212, 105)

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
            icon.setColor(if (state && color) BUTTON_HOVER else Color(150, 150, 150, 255))
        }

        onMouseLeave {
            icon.setColor(if (state && color) VigilancePalette.getAccent() else Color.WHITE.withAlpha(255))
        }
    }

    fun updateState(state: Boolean) {
        if (this.state != state) {
            this.state = state
            icon.setSVG(if (state) clicked else original)
            icon.setColor(if (state && color) VigilancePalette.getAccent() else Color.WHITE.withAlpha(255))
        }
    }

    private fun updateStateWithHover(state: Boolean) {
        this.state = state
        icon.setSVG(if (state) clicked else original)
        icon.setColor(
            if (state && color)
                (if (this.isHovered()) BUTTON_HOVER else VigilancePalette.getAccent())
            else
                (if (this.isHovered()) Color(150, 150, 150, 255) else Color.WHITE.withAlpha(255))
        )
    }
}
