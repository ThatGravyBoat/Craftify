package tech.thatgravyboat.craftify.ui

import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.universal.ChatColor
import gg.essential.universal.UChat
import gg.essential.universal.utils.MCClickEventAction
import gg.essential.universal.wrappers.message.UTextComponent
import tech.thatgravyboat.craftify.Initializer
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.screens.volume.VolumeScreen
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.ui.enums.LinkingMode
import tech.thatgravyboat.craftify.utils.Utils
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

    private val positionButton by UIButton(URL(position), URL(position), click = {
        Utils.openScreen(PositionEditorScreen())
        false
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        x = SiblingConstraint(padding = 2f)
    } childOf this

    private val settingsButton by UIButton(URL(settings), URL(settings), click = {
        Config.gui()?.let { it1 -> Utils.openScreen(it1) }
        false
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        x = SiblingConstraint(padding = 2f)
    } childOf this

    private val shuffleButton by UIButton(URL(shuffle), URL(shuffle), true, click = { state -> Initializer.getAPI()?.setShuffle(!state) ?: false }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = SiblingConstraint(padding = 2f)
    } childOf this

    private val prevButton by UIButton(URL(prev), URL(prev), click = { Initializer.getAPI()?.prev() ?: false }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        x = SiblingConstraint(padding = 2f)
    } childOf this

    private val playButton by UIButton(URL(play), URL(pause), click = { state ->
        Player.stopClient()
        Initializer.getAPI()?.setPaused(state) ?: false
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        x = SiblingConstraint(padding = 2f)
    } childOf this

    private val nextButton by UIButton(URL(next), URL(next), click = { Initializer.getAPI()?.next() ?: false }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        x = SiblingConstraint(padding = 2f)
    } childOf this

    private val repeatButton by UIButton(URL(repeat), URL(repeat), true, click = { state -> Initializer.getAPI()?.setRepeat(if (state) RepeatState.OFF else RepeatState.SONG) ?: false }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        x = SiblingConstraint(padding = 2f)
    } childOf this

    private val externalButton by UIButton(URL(external), URL(external), click = {
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
        x = SiblingConstraint(padding = 2f)
    } childOf this

    private val volumeButton by UIButton(URL(volume), URL(volume), click = {
        Utils.openScreen(VolumeScreen())
        false
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        x = SiblingConstraint(padding = 2f)
    } childOf this

    fun updateState(state: State) {
        repeatButton.updateState(state.player.repeat != RepeatState.OFF)
        shuffleButton.updateState(state.isShuffling)
        playButton.updateState(state.isPlaying)
    }

    fun updateTheme() {

        var activeButtons = updateButton(settingsButton, ThemeConfig.showSettingsButton, ThemeConfig.settingsIcon, settings)
        activeButtons = updateButton(shuffleButton, ThemeConfig.showShuffleButton, ThemeConfig.shuffleIcon, shuffle, activeButtons)
        activeButtons = updateButton(prevButton, ThemeConfig.showPreviousButton, ThemeConfig.previousIcon, prev, activeButtons)
        activeButtons = updateButton(nextButton, ThemeConfig.showNextButton, ThemeConfig.nextIcon, next, activeButtons)
        activeButtons = updateButton(repeatButton, ThemeConfig.showRepeatButton, ThemeConfig.repeatIcon, repeat, activeButtons)
        activeButtons = updateButton(externalButton, ThemeConfig.showExternalButton, ThemeConfig.externalIcon, external, activeButtons)
        activeButtons = updateButton(playButton, ThemeConfig.showPlayButton, ThemeConfig.pauseIcon, pause, activeButtons, ThemeConfig.playIcon, play)
        activeButtons = updateButton(volumeButton, ThemeConfig.showVolumeButton && (!ThemeConfig.hideImage || activeButtons < 7), ThemeConfig.volumeIcon, volume, activeButtons)
        activeButtons = updateButton(positionButton, ThemeConfig.showPositionEditorButton && (!ThemeConfig.hideImage || activeButtons < 7), ThemeConfig.positionEditorIcon, position, activeButtons)
    }

    private fun updateButton(button: UIButton, visible: Boolean, image: String, default: String, activeButtons: Int = 0, ogimage: String? = null, ogdefault: String? = null): Int {
        if (!visible) {
            button.hide(true)
            return activeButtons
        }

        button.unhide()
        if (ogimage == null || ogdefault == null) {
            button.updateImage(notNullNotBlankOrElse(image, default))
        } else {
            button.updateImage(notNullNotBlankOrElse(image, default), notNullNotBlankOrElse(ogimage, ogdefault))
        }
        return activeButtons + 1
    }

    private fun notNullNotBlankOrElse(input: String?, default: String): String {
        return if (input.isNullOrBlank()) default else input
    }
}
