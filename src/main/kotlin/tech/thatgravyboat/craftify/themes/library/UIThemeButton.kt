package tech.thatgravyboat.craftify.themes.library

import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.AspectConstraint
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.vigilance.gui.VigilancePalette
import java.net.URL
class UIThemeButton(iconUrl: URL, title: String, author: String) : UIBlock(VigilancePalette.getHighlight()) {

    private val iconContainer = UIContainer().constrain {
        height = 100.percent()
        width = AspectConstraint()
    } childOf this

    init {

        onMouseEnter {
            setColor(VigilancePalette.getBrightHighlight())
        }

        onMouseLeave {
            setColor(VigilancePalette.getHighlight())
        }

        UIImage.ofURL(iconUrl).constrain {
            height = 80.percent()
            width = 80.percent()
            x = CenterConstraint()
            y = CenterConstraint()
        } childOf iconContainer
    }

    val text = UIWrappedText("$title\n\nBy $author").constrain {
        x = SiblingConstraint()
        y = CenterConstraint()
        width = 100.percent()
    } childOf this
}
