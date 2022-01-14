package tech.thatgravyboat.craftify.ui.enums.displaying

import tech.thatgravyboat.craftify.types.PlayerState

interface DisplayCheck {
    fun canDisplay(state: PlayerState?): Boolean
}
