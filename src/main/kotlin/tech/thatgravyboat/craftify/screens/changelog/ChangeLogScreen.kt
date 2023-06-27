package tech.thatgravyboat.craftify.screens.changelog

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.markdown.MarkdownComponent
import gg.essential.universal.GuiScale
import gg.essential.vigilance.gui.VigilancePalette

class ChangeLogScreen(markdown: String) : WindowScreen(version = ElementaVersion.V2, newGuiScale = GuiScale.scaleForScreenSize().ordinal, restoreCurrentGuiOnClose = true) {

    init {
        UIBlock(VigilancePalette.getBackground()).constrain {
            width = 100.percent()
            height = 100.percent()
        } childOf window
    }

    private val container by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 85.percent()
        height = 75.percent()
    } childOf window

    private val leftDivider by UIBlock(VigilancePalette.getDivider()).constrain {
        x = (-1).pixel()
        width = 1.pixel()
        height = 100.percent()
    } childOf container

    init {
        UIBlock(VigilancePalette.getDivider()).constrain {
            x = SiblingConstraint() boundTo container
            width = 1.pixel()
            height = 100.percent()
        } childOf container
    }

    private val titleBar by UIBlock(VigilancePalette.getLightBackground()).constrain {
        width = 100.percent()
        height = 30.pixels()
    } childOf container

    init {
        UIWrappedText("Change Log").constrain {
            x = 11.pixels()
            y = CenterConstraint()
        } childOf titleBar
    }

    private val content by ScrollComponent().constrain {
        x = RelativeConstraint() boundTo leftDivider
        y = RelativeConstraint() boundTo titleBar
        width = 100.percent()
        height = 100.percent() - 30.pixels()
    } childOf container

    init {
        MarkdownComponent(markdown).constrain {
            x = 5.percent()
            y = 5.percent()
            width = 90.percent()
            height = 90.percent()
        } childOf content
    }
}
