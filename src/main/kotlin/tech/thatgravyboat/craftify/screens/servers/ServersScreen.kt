package tech.thatgravyboat.craftify.screens.servers

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.*
import gg.essential.elementa.constraints.AspectConstraint
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.font.DefaultFonts
import gg.essential.universal.utils.MCScreen
import gg.essential.vigilance.gui.VigilancePalette
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.platform.network.PacketHandler
import tech.thatgravyboat.craftify.utils.RenderUtils.scrollGradient

class ServersScreen(parent: MCScreen? = currentScreen) : WindowScreen(version = ElementaVersion.V2, restoreCurrentGuiOnClose = true) {

    private val container by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        height = 75.percent()
        width = AspectConstraint(0.75f)
    } childOf window

    init {
        UIBlock(VigilancePalette.getMainBackground()).apply {
            constrain {
                height = 100.percent()
                width = 100.percent()
            } childOf container
            enableEffect(OutlineEffect(VigilancePalette.getScrollbar(), 1f))
        }

        UIText("Share to Servers").constrain {
            x = CenterConstraint()
            y = 20.pixels()
        } childOf container
    }

    init {
        val halfServerTextWidth = DefaultFonts.MINECRAFT_FIVE.getStringWidth("Servers", 10f) / 2f
        UIText(text = "Servers", shadow = false).constrain {
            x = CenterConstraint()
            y = 50.pixels()
            fontProvider = DefaultFonts.MINECRAFT_FIVE
            color = VigilancePalette.getText().constraint
        } childOf container

        UIBlock(VigilancePalette.getText()).constrain {
            x = 5.percent()
            y = 50.pixels() + (DefaultFonts.MINECRAFT_FIVE.getBaseLineHeight() / 2f).pixels()
            width = 45.percent() - halfServerTextWidth.pixels() - 10.pixels()
            height = 1.pixels()
        } childOf container

        UIBlock(VigilancePalette.getText()).constrain {
            x = 50.percent() + halfServerTextWidth.pixels() + 10.pixels()
            y = 50.pixels() + (DefaultFonts.MINECRAFT_FIVE.getBaseLineHeight() / 2f).pixels()
            width = 45.percent() - halfServerTextWidth.pixels() - 10.pixels()
            height = 1.pixels()
        } childOf container
    }

    private val list by ScrollComponent().apply {
        val box = UIContainer().constrain {
            x = CenterConstraint()
            y = 60.pixels()
            width = 90.percent()
            height = 60.percent()
        } childOf container

        constrain {
            width = 100.percent() - 4.pixels()
            height = 100.percent()
        } childOf box

        val scrollBar = UIBlock(VigilancePalette.getScrollbar()).constrain {
            x = SiblingConstraint() + 1.pixels()
            width = 2.pixels()
        } childOf box

        setScrollBarComponent(scrollBar, hideWhenUseless = true, isHorizontal = false)
        this.scrollGradient(20.pixels())
    }

    init {
        val servers = Config.allowedServers.split("\n").map { it.trim() }.toMutableList()
        val closeBox = UIBlock(VigilancePalette.getButton()).constrain {
            x = 50.percent() - 65.pixels()
            y = 60.pixels() + 60.percent() + 5.pixels()
            width = 50.pixels()
            height = 15.pixels()
        }.onMouseEnter {
            setColor(VigilancePalette.getButtonHighlight())
        }.onMouseLeave {
            setColor(VigilancePalette.getButton())
        }.onMouseClick {
            displayScreen(parent)
        } childOf container

        UIText(text = "Close", shadow = false).constrain {
            x = CenterConstraint()
            y = CenterConstraint()
        } childOf closeBox

        val canAdd = PacketHandler.getServerAddress().isNotBlank() && !PacketHandler.isOnLanServer()

        val addButton = UIBlock(if (canAdd) VigilancePalette.getButton() else VigilancePalette.getComponentBackground()).constrain {
            x = 50.percent() + 15.pixels()
            y = 60.pixels() + 60.percent() + 5.pixels()
            width = 50.pixels()
            height = 15.pixels()
        }.onMouseEnter {
            if (canAdd) {
                setColor(VigilancePalette.getButtonHighlight())
            }
        }.onMouseLeave {
            if (canAdd) {
                setColor(VigilancePalette.getButton())
            }
        }.onMouseClick {
            if (canAdd) {
                if (servers.contains(PacketHandler.getServerAddress())) {
                    servers.remove(PacketHandler.getServerAddress())
                }
                servers.add(0, PacketHandler.getServerAddress())
                updateServers(servers)
                Config.markDirty()
                Config.writeData()
            }
        } childOf container

        UIText(text = "Add", shadow = false).constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            color = if (canAdd) VigilancePalette.getText().constraint else VigilancePalette.getTextDisabled().constraint
        } childOf addButton

        updateServers(servers)
    }

    private fun updateServers(servers: MutableList<String>) {
        list.clearChildren()
        servers.forEach { ip ->
            val server = UIBlock(VigilancePalette.getComponentBackground()).constrain {
                x = CenterConstraint()
                y = SiblingConstraint() + 2.pixels()
                width = 100.percent()
                height = 20.pixels()
            } effect OutlineEffect(VigilancePalette.getComponentBorder(), 1f, drawInsideChildren = true) childOf list

            val button = UIBlock(VigilancePalette.getButton()).constrain {
                x = 100.percent() - 16.pixels()
                y = CenterConstraint()
                width = 12.pixels()
                height = 12.pixels()
            }.onMouseEnter {
                setColor(VigilancePalette.getButtonHighlight())
            }.onMouseLeave {
                setColor(VigilancePalette.getButton())
            }.onMouseClick {
                servers.remove(ip)
                updateServers(servers)
                Config.markDirty()
                Config.writeData()
            } childOf server

            UIText(text = "-", shadow = false).constrain {
                x = CenterConstraint()
                y = CenterConstraint()
            } childOf button

            UIWrappedText(ip).constrain {
                x = 5.pixels()
                y = CenterConstraint()
                width = 100.percent() - 10.pixels() - 12.pixels() - 5.pixels()
                height = 10.pixels()
            } childOf server
        }
        Config.allowedServers = servers.joinToString("\n")
    }
}
