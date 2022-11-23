package tech.thatgravyboat.craftify.themes.library

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.universal.GuiScale
import gg.essential.vigilance.gui.VigilancePalette

class LibraryScreen : WindowScreen(version = ElementaVersion.V1, newGuiScale = GuiScale.scaleForScreenSize().ordinal, restoreCurrentGuiOnClose = true) {

    private var selectedId = "default"

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

    init {
        UIBlock(VigilancePalette.getDivider()).constrain {
            x = (-1).pixel()
            width = 1.pixel()
            height = 100.percent()
        } childOf container
    }

    private val rightDivider by UIBlock(VigilancePalette.getDivider()).constrain {
        x = SiblingConstraint() boundTo container
        width = 1.pixel()
        height = 100.percent()
    } childOf container

    private val titleBar by UIBlock(VigilancePalette.getLightBackground()).constrain {
        width = 100.percent()
        height = 30.pixels()
    } childOf container

    private val middleDivider by UIBlock(VigilancePalette.getDivider()).constrain {
        x = (SiblingConstraint(alignOpposite = true) boundTo container) + 30.percent()
        width = 1.pixel()
        height = 100.percent()
    } childOf container

    init {
        UIWrappedText("Theme Library").constrain {
            x = 11.pixels()
            y = CenterConstraint()
        } childOf titleBar
    }

    private val content by UIContainer().constrain {
        x = RelativeConstraint() boundTo middleDivider
        y = RelativeConstraint() boundTo titleBar
        width = 70.percent()
        height = 100.percent() - 30.pixels()
    } childOf container

    private var info: UIThemeInfo? = null

    val themes by ScrollComponent("No Themes Found").constrain {
        y = RelativeConstraint() boundTo titleBar
        height = 100.percent() - 30.pixels()
        width = 30.percent()
    } childOf container

    private val themeTitle = UIText().constrain {
        x = (RelativeConstraint() boundTo middleDivider) + 11.pixels()
        y = CenterConstraint()
    } childOf titleBar

    private val install = UIThemeInstallButton("Install").apply {
        constrain {
            x = (SiblingConstraint(alignOpposite = true) boundTo rightDivider) - 10.pixels()
            y = CenterConstraint()
        } childOf titleBar

        this.onMouseClick {
            LibraryStorage.getTheme(selectedId).theme.setConfig()
            this@apply.setText("Installed!")
        }
    }

    init {
        install.hide(true)

        for (theme in LibraryStorage.getThemes()) {
            UIThemeButton(theme.icon, theme.displayName, theme.author).apply {
                constrain {
                    y = SiblingConstraint(padding = 2f)
                    width = 100.percent() - 1.pixel()
                    height = 50.pixel()
                } childOf themes
                onMouseClick {
                    selectPage(theme.id)
                }
            }
        }
    }

    private fun selectPage(id: String) {
        this.selectedId = id
        val theme = LibraryStorage.getTheme(id)
        themeTitle.setText("${theme.displayName} by ${theme.author}")
        install.unhide(true)
        install.setText("Install")

        info?.let { content.removeChild(it) }
        info = UIThemeInfo(theme.screenshot, theme.description).constrain {
            width = 100.percent()
            height = 100.percent()
        } childOf content
    }
}
