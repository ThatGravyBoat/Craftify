package tech.thatgravyboat.craftify

import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.SubCommand
import gg.essential.api.utils.GuiUtil
import tech.thatgravyboat.craftify.api.SpotifyAPI

object Command : Command("craftify") {

    @DefaultHandler
    fun handle() {
        Config.gui()?.let { GuiUtil.open(it) }
    }

    @SubCommand("start")
    fun onToken() {
        SpotifyAPI.startPoller()
    }

    @SubCommand("stop")
    fun stop() {
        SpotifyAPI.stopPoller()
    }
}
