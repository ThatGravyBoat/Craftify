package tech.thatgravyboat.craftify.ui

import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.dsl.*
import gg.essential.universal.ChatColor
import gg.essential.universal.UChat
import gg.essential.universal.UScreen
import gg.essential.universal.utils.MCClickEventAction
import gg.essential.universal.wrappers.message.UTextComponent
import tech.thatgravyboat.craftify.Initializer
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.screens.volume.VolumeScreen
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.ui.enums.LinkingMode
import tech.thatgravyboat.craftify.utils.EssentialApiHelper
import tech.thatgravyboat.jukebox.api.state.RepeatState
import tech.thatgravyboat.jukebox.api.state.State
import java.net.URI
import java.net.URL

class UIControls : UIContainer() {

    private val play = "https://i.imgur.com/JQdBt2K.png"
    private val pause = "https://i.imgur.com/9tsZMcO.png"
    private val shuffle = "https://i.imgur.com/W58UJGf.png"
    private val repeat = "https://i.imgur.com/C8h1RBc.png"
    private val next = "https://i.imgur.com/4L2322Q.png"
    private val prev = "https://i.imgur.com/Lb4YYOu.png"
    private val external = "https://i.imgur.com/qQs0WHt.png"
    private val volume = "https://i.imgur.com/RNfbruf.png"
    private val position = "https://i.imgur.com/XZWUSSe.png"
    private val settings = "https://i.imgur.com/Nd4gQzY.png"

    private val positionButton = UIButton(URL(position), URL(position), click = {
        EssentialApiHelper.openScreen(PositionEditorScreen())
        false
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels() - 48.pixels()
    } childOf this

    private val settingsButton = UIButton(URL(settings), URL(settings), click = {
        Config.gui()?.let { it1 -> EssentialApiHelper.openScreen(it1) }
        false
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels() - 36.pixels()
    } childOf this

    private val shuffleButton = UIButton(URL(shuffle), URL(shuffle), true, click = { state -> Initializer.getAPI()?.setShuffle(!state) ?: false }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels() - 24.pixels()
    } childOf this

    private val prevButton = UIButton(URL(prev), URL(prev), click = { Initializer.getAPI()?.prev() ?: false }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels() - 12.pixels()
    } childOf this

    private val playButton = UIButton(URL(play), URL(pause), click = { state ->
        Player.stopClient()
        Initializer.getAPI()?.setPaused(state) ?: false
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels()
    } childOf this

    private val nextButton = UIButton(URL(next), URL(next), click = { Initializer.getAPI()?.next() ?: false }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels() + 12.pixels()
    } childOf this

    private val repeatButton = UIButton(URL(repeat), URL(repeat), true, click = { state -> Initializer.getAPI()?.setRepeat(if (state) RepeatState.OFF else RepeatState.SONG) ?: false }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels() + 24.pixels()
    } childOf this

    private val externalButton = UIButton(URL(external), URL(external), click = {
        Initializer.getAPI()?.getState()?.let {
            val linkingMode = LinkingMode.values()[Config.linkMode]
            if (!linkingMode.copy(URI(it.song.url))) {
                val component = UTextComponent("${ChatColor.GREEN}Craftify > ${ChatColor.GRAY} $it")
                component.setClick(MCClickEventAction.OPEN_URL, it.song.url)
                UChat.chat(component)
            }
        }
        false
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels() + 36.pixels()
    } childOf this

    private val volumeButton = UIButton(URL(volume), URL(volume), click = {
        EssentialApiHelper.openScreen(VolumeScreen())
        false
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels() + 48.pixels()
    } childOf this

    fun updateState(state: State) {
        repeatButton.updateState(state.player.repeat != RepeatState.OFF)
        shuffleButton.updateState(state.isShuffling)
        playButton.updateState(state.isPlaying)
    }

    fun updateTheme() {
        settingsButton.updateImage(notNullNotBlankOrElse(ThemeConfig.settingsIcon, settings))
        shuffleButton.updateImage(notNullNotBlankOrElse(ThemeConfig.shuffleIcon, shuffle))
        prevButton.updateImage(notNullNotBlankOrElse(ThemeConfig.previousIcon, prev))
        playButton.updateImage(notNullNotBlankOrElse(ThemeConfig.pauseIcon, pause), notNullNotBlankOrElse(ThemeConfig.playIcon, play))
        nextButton.updateImage(notNullNotBlankOrElse(ThemeConfig.nextIcon, next))
        repeatButton.updateImage(notNullNotBlankOrElse(ThemeConfig.repeatIcon, repeat))
        externalButton.updateImage(notNullNotBlankOrElse(ThemeConfig.externalIcon, external))
        volumeButton.updateImage(notNullNotBlankOrElse(ThemeConfig.volumeIcon, volume))
        positionButton.updateImage(notNullNotBlankOrElse(ThemeConfig.positionEditorIcon, position))
    }

    private fun notNullNotBlankOrElse(input: String?, default: String): String {
        return if (input.isNullOrBlank()) default else input
    }
}
