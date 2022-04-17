package tech.thatgravyboat.craftify.platform

import gg.essential.universal.UMatrixStack
import gg.essential.universal.utils.MCScreen
import me.kbrewster.eventbus.eventbus
import me.kbrewster.eventbus.invokers.LMFInvoker

val eventBus = eventbus {
    invoker { LMFInvoker() }
}

open class Event

open class CancellableEvent(var cancelled: Boolean = false) : Event()

class TickEvent : Event()

class RenderEvent(val matrixStack: UMatrixStack) : Event()

class MouseClickEvent(val button: Int) : CancellableEvent()

class ScreenOpenEvent(val gui: MCScreen?) : Event()