package tech.thatgravyboat.craftify.platform

import gg.essential.universal.UMatrixStack
import gg.essential.universal.utils.MCScreen

object Events {
    val TICK = EventType<Unit>()
    val RENDER = EventType<UMatrixStack>()
    val MOUSE_CLICKED = CancellableEventType<Int>()
    val SCREEN_CHANGED = EventType<MCScreen?>()
}