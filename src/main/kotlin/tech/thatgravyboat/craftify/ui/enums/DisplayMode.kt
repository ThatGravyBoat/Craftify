package tech.thatgravyboat.craftify.ui.enums

import tech.thatgravyboat.jukebox.api.state.State

enum class DisplayMode {
    ALL_THE_TIME {
        override fun canDisplay(state: State?) = true
    },

    WHEN_PLAYING {
        override fun canDisplay(state: State?): Boolean = state?.isPlaying ?: false
    },

    WHEN_SOUND_FOUND {
        override fun canDisplay(state: State?) = state?.song != null
    };

    open fun canDisplay(state: State?): Boolean {
        throw NotImplementedError()
    }
}
