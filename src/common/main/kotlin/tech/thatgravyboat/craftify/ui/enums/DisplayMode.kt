package tech.thatgravyboat.craftify.ui.enums

import tech.thatgravyboat.jukebox.api.state.State

enum class DisplayMode(val check: (State?) -> Boolean) {
    ALL_THE_TIME({ true }),
    WHEN_PLAYING({ it?.isPlaying == true }),
    WHEN_SONG_FOUND({ it?.song != null }),
    ;

    fun canDisplay(state: State?): Boolean = this.check(state)

    override fun toString(): String = when (this) {
        ALL_THE_TIME -> "All the Time"
        WHEN_PLAYING -> "When Playing"
        WHEN_SONG_FOUND -> "When Song Found"
    }
}
