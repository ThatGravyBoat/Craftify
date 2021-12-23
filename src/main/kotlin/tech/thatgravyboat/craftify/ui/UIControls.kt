package tech.thatgravyboat.craftify.ui

import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.dsl.*
import gg.essential.elementa.svg.SVGParser
import tech.thatgravyboat.craftify.api.SpotifyAPI
import tech.thatgravyboat.craftify.types.PlayerState

class UIControls : UIContainer() {

    private val play = SVGParser.parseFromResource("/assets/craftify/svg/play.svg")
    private val pause = SVGParser.parseFromResource("/assets/craftify/svg/pause.svg")
    private val shuffle = SVGParser.parseFromResource("/assets/craftify/svg/shuffle.svg")
    private val repeat = SVGParser.parseFromResource("/assets/craftify/svg/repeat.svg")
    private val next = SVGParser.parseFromResource("/assets/craftify/svg/next.svg")
    private val prev = SVGParser.parseFromResource("/assets/craftify/svg/prev.svg")

    private val shuffleButton = UIButton(shuffle, shuffle, true, click = { state -> SpotifyAPI.toggleShuffle(state) }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 70.pixels() - 24.pixels()
    } childOf this

    init {
        UIButton(prev, prev, click = { SpotifyAPI.skip(false) }).constrain {
            width = 10.pixels()
            height = 10.pixels()
            y = 0.pixels()
            x = 70.pixels() - 12.pixels()
        } childOf this
    }

    private val playButton = UIButton(play, pause, click = { state ->
        SpotifyAPI.changePlayingState(state)
        Player.stopClient()
    }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 70.pixels()
    } childOf this

    init {
        UIButton(next, next, click = { SpotifyAPI.skip(true) }).constrain {
            width = 10.pixels()
            height = 10.pixels()
            y = 0.pixels()
            x = 70.pixels() + 12.pixels()
        } childOf this
    }

    private val repeatButton = UIButton(repeat, repeat, true, click = { state -> SpotifyAPI.toggleRepeat(state) }).constrain {
        width = 10.pixels()
        height = 10.pixels()
        y = 0.pixels()
        x = 70.pixels() + 24.pixels()
    } childOf this

    fun updateState(state: PlayerState) {
        repeatButton.updateState(state.isRepeating())
        shuffleButton.updateState(state.isShuffling())
        playButton.updateState(state.isPlaying())
    }
}
