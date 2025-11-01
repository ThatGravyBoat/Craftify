package tech.thatgravyboat.craftify.ui.v2

import earth.terrarium.olympus.client.components.Widgets
import earth.terrarium.olympus.client.layouts.Layouts
import earth.terrarium.olympus.client.layouts.LinearViewLayout
import gg.essential.universal.ChatColor
import gg.essential.universal.UChat
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import tech.thatgravyboat.craftify.Craftify
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.platform.McClient
import tech.thatgravyboat.craftify.screens.volume.VolumeScreen
import tech.thatgravyboat.craftify.services.ServiceHelper
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.screens.PositionEditorScreen
import tech.thatgravyboat.craftify.utils.Utils
import tech.thatgravyboat.craftify.utils.isEmpty
import tech.thatgravyboat.craftify.utils.toggle
import tech.thatgravyboat.jukebox.api.service.ServiceFunction
import tech.thatgravyboat.jukebox.api.state.RepeatState
import tech.thatgravyboat.jukebox.api.state.ShuffleState
import java.net.URI
import earth.terrarium.olympus.client.utils.State as OlympusState
import tech.thatgravyboat.jukebox.api.state.State as JukeboxState

object UIPlayerControls {

    private val shuffle = OlympusState.of(false)
    private val repeat = OlympusState.of(false)
    private val playing = OlympusState.of(false)

    private fun LinearViewLayout.withButton(
        enabled: Boolean,
        normal: String,
        selected: String = normal,
        state: OlympusState<Boolean?> = OlympusState.empty(),
        action: () -> Unit
    ): LinearViewLayout {
        if (!enabled) return this
        return this.withChild(
            Widgets.button()
                .withTexture(null)
                .withCallback {
                    action()
                    if (!state.isEmpty()) state.toggle()
                }
                .withRenderer(ControlButtonWidgetRenderer(normal, selected, state))
                .withSize(10)
        )
    }

    fun create(state: JukeboxState): LinearViewLayout {
        this.repeat.set(state.player.repeat == RepeatState.ALL || state.player.repeat == RepeatState.SONG)
        this.shuffle.set(state.player.shuffle == ShuffleState.ON)
        this.playing.set(state.songState.isPlaying)

        val canUseShuffle = ServiceHelper.doesSupport(ServiceFunction.SHUFFLE)
        val canUseRepeat = ServiceHelper.doesSupport(ServiceFunction.REPEAT)
        val canUseVolume = ServiceHelper.doesSupport(ServiceFunction.VOLUME)

        return Layouts.row().withGap(2)
            .withButton(ThemeConfig.showPositionEditorButton, ThemeConfig.positionEditorIcon) {
                McClient.screen = PositionEditorScreen()
            }
            .withButton(ThemeConfig.showSettingsButton, ThemeConfig.settingsIcon) {
                Config.gui()?.let { it1 -> Utils.openScreen(it1) }
            }
            .withButton(ThemeConfig.showShuffleButton && canUseShuffle, ThemeConfig.shuffleIcon, state = this.shuffle) {
                Craftify.service?.toggleShuffle()
            }
            .withButton(ThemeConfig.showPreviousButton, ThemeConfig.previousIcon) {
                Craftify.service?.prev()
            }
            .withButton(ThemeConfig.showPlayButton, ThemeConfig.playIcon, ThemeConfig.pauseIcon, this.playing) {
                Craftify.service?.setPaused(state.isPlaying)
            }
            .withButton(ThemeConfig.showNextButton, ThemeConfig.nextIcon) {
                Craftify.service?.next()
            }
            .withButton(ThemeConfig.showRepeatButton && canUseRepeat, ThemeConfig.repeatIcon, state = this.repeat) {
                Craftify.service?.toggleRepeat()
            }
            .withButton(ThemeConfig.showExternalButton, ThemeConfig.externalIcon) {
                val uri = URI(state.song.url)
                if (!Config.linkMode.copy(uri)) {
                    UChat.chat(Component.literal("${ChatColor.GREEN}Craftify > ${ChatColor.GRAY} $uri").also {
                        it.withStyle { style -> style.withClickEvent(ClickEvent.OpenUrl(uri)) }
                    })
                }
            }
            .withButton(ThemeConfig.showVolumeButton && canUseVolume, ThemeConfig.volumeIcon) {
                Utils.openScreen(VolumeScreen())
            }
    }
}