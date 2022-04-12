package tech.thatgravyboat.craftify

import gg.essential.universal.UMatrixStack
import gg.essential.universal.utils.MCScreen
import me.kbrewster.eventbus.eventbus
import me.kbrewster.eventbus.invokers.LMFInvoker

//#if MODERN==0
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//#endif

val eventBus = eventbus {
    invoker { LMFInvoker() }
}

open class Event

open class CancellableEvent(var cancelled: Boolean = false) : Event()

class TickEvent : Event()

class RenderEvent(val matrixStack: UMatrixStack) : Event()

class MouseClickEvent(val button: Int) : CancellableEvent()

class ScreenOpenEvent(val gui: MCScreen?) : Event()

object Events {
    init {
        //#if MODERN==0
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this)
        //#else
            //#if FABRIC==1
            //$$ net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.START_CLIENT_TICK.register { eventBus.post(TickEvent()) }
            //$$ net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback.EVENT.register { matrixStack, _ ->
            //$$            eventBus.post(RenderEvent(UMatrixStack(matrixStack)))
            //$$        }
            //#endif
        //#endif
    }

    //#if MODERN==0
    @SubscribeEvent
    fun onFirstLoad(event: net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent) {
        if (event.phase == net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START) return
        eventBus.post(TickEvent())
    }

    @SubscribeEvent
    fun onRender(event: net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent) {
        if (event.phase == net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START) return
        eventBus.post(RenderEvent(UMatrixStack.Compat.get()))
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onMouseClicked(mouseEvent: net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent.Pre) {
        val shouldContinue = org.lwjgl.input.Mouse.getEventButtonState()
        val button = org.lwjgl.input.Mouse.getEventButton()
        if (shouldContinue) {
            val event = MouseClickEvent(button)
            eventBus.post(event)
            if (event.cancelled) mouseEvent.isCanceled = true
        }
    }

    @SubscribeEvent
    fun onGuiClose(event: GuiOpenEvent) {
        eventBus.post(ScreenOpenEvent(event.gui))
    }
    //#endif
}