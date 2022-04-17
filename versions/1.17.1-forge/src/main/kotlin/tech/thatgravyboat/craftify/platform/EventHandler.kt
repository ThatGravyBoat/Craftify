package tech.thatgravyboat.craftify.platform

import gg.essential.universal.UMatrixStack
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent

object EventHandler {

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onFirstLoad(event: net.minecraftforge.event.TickEvent.ClientTickEvent) {
        if (event.phase == net.minecraftforge.event.TickEvent.Phase.START) return
        eventBus.post(TickEvent())
    }

    @SubscribeEvent
    fun onRender(event: net.minecraftforge.event.TickEvent.RenderTickEvent) {
        if (event.phase == net.minecraftforge.event.TickEvent.Phase.START) return
        eventBus.post(RenderEvent(UMatrixStack.Compat.get()))
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onMouseClicked(mouseEvent: net.minecraftforge.client.event.GuiScreenEvent.MouseClickedEvent.Pre) {
        val event = MouseClickEvent(mouseEvent.button)
        eventBus.post(event)
        if (event.cancelled) mouseEvent.isCanceled = true
    }

    @SubscribeEvent
    fun onGuiClose(event: GuiOpenEvent) {
        eventBus.post(ScreenOpenEvent(event.gui))
    }
}