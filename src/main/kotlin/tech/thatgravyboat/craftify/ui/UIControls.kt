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
import tech.thatgravyboat.craftify.services.ServiceHelper
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.ui.enums.LinkingMode
import tech.thatgravyboat.craftify.utils.Utils
import tech.thatgravyboat.jukebox.api.service.ServiceFunction
import tech.thatgravyboat.jukebox.api.state.RepeatState
import tech.thatgravyboat.jukebox.api.state.State
import java.net.URI
import java.net.URL

class UIControls : UIContainer() {

    private val play = "https://files.teamresourceful.com/r/rlQyJs.png"
    private val pause = "https://files.teamresourceful.com/r/55kubx.png"
    private val shuffle = "https://files.teamresourceful.com/r/Bl9ZTS.png"
    private val repeat = "https://files.teamresourceful.com/r/4gVqmu.png"
    private val next = "https://files.teamresourceful.com/r/FDIAxt.png"
    private val prev = "https://files.teamresourceful.com/r/Dr7nH1.png"
    private val external = "https://files.teamresourceful.com/r/7gJ4OY.png"
    private val volume = "https://files.teamresourceful.com/r/i7XLC1.png"
    private val position = "https://files.teamresourceful.com/r/N3c8xm.png"
    private val settings = "https://files.teamresourceful.com/r/9DzwrP.png"

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

    private val shuffleButton by UIButton(URL(shuffle), URL(shuffle), true, click = {
        Initializer.getAPI()?.toggleShuffle() == true
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = SiblingConstraint(padding = 2f)
    } childOf this

    private val prevButton by UIButton(URL(prev), URL(prev), click = {
        Initializer.getAPI()?.prev() == true
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        x = SiblingConstraint(padding = 2f)
    } childOf this

    private val playButton by UIButton(URL(play), URL(pause), click = { state ->
        Player.stopClient()
        Initializer.getAPI()?.setPaused(state) == true
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        x = SiblingConstraint(padding = 2f)
    } childOf this

    private val nextButton by UIButton(URL(next), URL(next), click = {
        Initializer.getAPI()?.next() == true
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        x = SiblingConstraint(padding = 2f)
    } childOf this

    private val repeatButton by UIButton(URL(repeat), URL(repeat), true, click = { state ->
        Initializer.getAPI()?.toggleRepeat() == true
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        x = SiblingConstraint(padding = 2f)
    } childOf this

    private val externalButton by UIButton(URL(external), URL(external), click = {
        Initializer.getAPI()?.getState()?.let {
            if (!Config.linkMode.copy(URI(it.song.url))) {
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
        if (ServiceHelper.doesSupport(ServiceFunction.VOLUME)) {
            Utils.openScreen(VolumeScreen())
        } else {
            UChat.chat("Your currently selected service does not support volume control.")
        }
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
