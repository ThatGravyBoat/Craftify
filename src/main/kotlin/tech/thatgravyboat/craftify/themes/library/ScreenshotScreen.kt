package tech.thatgravyboat.craftify.themes.library

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent

object ScreenshotScreen : WindowScreen(ElementaVersion.V1) {

    init {
        UIBlock().constrain {
            width = 100.percent()
            height = 100.percent()
        } childOf window
    }
}
