package tech.thatgravyboat.craftify.themes

import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.utils.GuiUtil

object ThemeCommand : Command("craftifytheme") {

    @DefaultHandler
    fun handle() {
        ThemeConfig.gui()?.let { GuiUtil.open(it) }
    }
}
