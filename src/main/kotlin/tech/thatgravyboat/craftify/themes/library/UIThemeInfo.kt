package tech.thatgravyboat.craftify.themes.library

import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.AspectConstraint
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.vigilance.gui.VigilancePalette
import java.net.URL

class UIThemeInfo(screenshot: URL, description: String) : UIContainer() {

    private val info = ScrollComponent("No Info Provided").constrain {
        width = 100.percent()
        height = 100.percent()
    } childOf this

    init {
        val image = UIImage.ofURL(screenshot).constrain {
            x = CenterConstraint()
            y = SiblingConstraint(5f) + 5.pixels()
            width = 75.percent()
            height = AspectConstraint(0.4f)
        } childOf info
        image.enableEffect(OutlineEffect(VigilancePalette.getOutline(), 2f))

        UIText("Preview").constrain {
            x = CenterConstraint()
            y = SiblingConstraint(5f)
        } childOf info

        UIText("Description:").constrain {
            x = 12.5.percent()
            y = SiblingConstraint(5f) + 5.pixels()
        } childOf info

        UIWrappedText(description).constrain {
            x = 12.5.percent()
            y = SiblingConstraint(5f)
            width = 75.percent()
        } childOf info
    }
}
