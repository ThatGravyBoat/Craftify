package tech.thatgravyboat.craftify.ui.enums.displaying

import tech.thatgravyboat.craftify.types.PlayerState

enum class DisplayMode : DisplayCheck {
    ALL_THE_TIME {
        override fun canDisplay(state: PlayerState?) = true
    },

    WHEN_PLAYING {
        override fun canDisplay(state: PlayerState?): Boolean = state?.isPlaying() ?: false
    },

    WHEN_SOUND_FOUND {
        override fun canDisplay(state: PlayerState?) = state?.item?.album?.uri != null
    }
}
