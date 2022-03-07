package tech.thatgravyboat.craftify.themes.library

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.vigilance.gui.VigilancePalette
import java.awt.Color

private val BUTTON_HOVER = Color(0, 212, 105)

class UIThemeInstallButton(text: String) : UIBlock(VigilancePalette.getAccent()) {

    private val uiText = UIText(text).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
    } childOf this

    init {

        this.setWidth((uiText.getWidth() + 20).pixels())

        constrain {
            height = 20.pixels()
        }

        onMouseEnter {
            this.setColor(BUTTON_HOVER)
        }

        onMouseLeave {
            this.setColor(VigilancePalette.getAccent())
        }
    }

    fun setText(text: String) {
        uiText.setText(text)
    }
}
