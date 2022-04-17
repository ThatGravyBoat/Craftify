package tech.thatgravyboat.craftify.platform

import gg.essential.universal.UMatrixStack
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object EventHandler {

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

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
}