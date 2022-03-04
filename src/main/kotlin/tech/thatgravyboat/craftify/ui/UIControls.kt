package tech.thatgravyboat.craftify.ui

import gg.essential.api.utils.GuiUtil
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.dsl.*
import tech.thatgravyboat.craftify.Config
import tech.thatgravyboat.craftify.api.SpotifyAPI
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.types.PlayerState
import java.net.URL

class UIControls : UIContainer() {

    private val play = "https://i.imgur.com/JQdBt2K.png"
    private val pause = "https://i.imgur.com/9tsZMcO.png"
    private val shuffle = "https://i.imgur.com/W58UJGf.png"
    private val repeat = "https://i.imgur.com/C8h1RBc.png"
    private val next = "https://i.imgur.com/4L2322Q.png"
    private val prev = "https://i.imgur.com/Lb4YYOu.png"
    private val external = "https://i.imgur.com/qQs0WHt.png"
    private val settings = "https://i.imgur.com/Nd4gQzY.png"

    private val settingsButton = UIButton(URL(settings), URL(settings), click = { Config.gui()?.let { it1 -> GuiUtil.open(it1) } }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 70.pixels() - 36.pixels()
    } childOf this

    private val shuffleButton = UIButton(URL(shuffle), URL(shuffle), true, click = { state -> SpotifyAPI.toggleShuffle(state) }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 70.pixels() - 24.pixels()
    } childOf this

    private val prevButton = UIButton(URL(prev), URL(prev), click = { SpotifyAPI.skip(false) }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 70.pixels() - 12.pixels()
    } childOf this

    private val playButton = UIButton(URL(play), URL(pause), click = { state ->
        SpotifyAPI.changePlayingState(state)
        Player.stopClient()
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 70.pixels()
    } childOf this

    private val nextButton = UIButton(URL(next), URL(next), click = { SpotifyAPI.skip(true) }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 70.pixels() + 12.pixels()
    } childOf this

    private val repeatButton = UIButton(URL(repeat), URL(repeat), true, click = { state -> SpotifyAPI.toggleRepeat(state) }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 70.pixels() + 24.pixels()
    } childOf this

    private val externalButton = UIButton(URL(external), URL(external), click = { SpotifyAPI.openTrack() }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 70.pixels() + 36.pixels()
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
        playButton.updateImage(notNullNotBlankOrElse(ThemeConfig.playIcon, play), notNullNotBlankOrElse(ThemeConfig.pauseIcon, pause))
        nextButton.updateImage(notNullNotBlankOrElse(ThemeConfig.nextIcon, next))
        repeatButton.updateImage(notNullNotBlankOrElse(ThemeConfig.repeatIcon, repeat))
        externalButton.updateImage(notNullNotBlankOrElse(ThemeConfig.externalIcon, external))
    }

    private fun notNullNotBlankOrElse(input: String?, default: String): String {
        return if (input.isNullOrBlank()) default else input
    }
}
