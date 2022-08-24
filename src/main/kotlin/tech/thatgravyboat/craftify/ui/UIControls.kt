package tech.thatgravyboat.craftify.ui

import gg.essential.api.utils.GuiUtil
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.dsl.*
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.Initializer
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.types.PlayerState
import tech.thatgravyboat.craftify.volume.VolumeScreen
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

    private val positionButton = UIButton(URL(position), URL(position), click = { GuiUtil.open(PositionEditorScreen()) }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels() - 48.pixels()
    } childOf this

    private val settingsButton = UIButton(URL(settings), URL(settings), click = { Config.gui()?.let { it1 -> GuiUtil.open(it1) } }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels() - 36.pixels()
    } childOf this

    private val shuffleButton = UIButton(URL(shuffle), URL(shuffle), true, click = { state -> Initializer.getAPI()?.toggleShuffle(state) }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels() - 24.pixels()
    } childOf this

    private val prevButton = UIButton(URL(prev), URL(prev), click = { Initializer.getAPI()?.skip(false) }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels() - 12.pixels()
    } childOf this

    private val playButton = UIButton(URL(play), URL(pause), click = { state ->
        Initializer.getAPI()?.changePlayingState(state)
        Player.stopClient()
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels()
    } childOf this

    private val nextButton = UIButton(URL(next), URL(next), click = { Initializer.getAPI()?.skip(true) }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels() + 12.pixels()
    } childOf this

    private val repeatButton = UIButton(URL(repeat), URL(repeat), true, click = { state -> Initializer.getAPI()?.toggleRepeat(state) }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels() + 24.pixels()
    } childOf this

    private val externalButton = UIButton(URL(external), URL(external), click = { Initializer.getAPI()?.openTrack() }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels() + 36.pixels()
    } childOf this

    private val volumeButton = UIButton(URL(volume), URL(volume), click = { GuiUtil.open(VolumeScreen()) }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 65.pixels() + 48.pixels()
    } childOf this

    fun updateState(state: PlayerState) {
        repeatButton.updateState(state.isRepeating())
        shuffleButton.updateState(state.isShuffling())
        playButton.updateState(state.isPlaying())
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
