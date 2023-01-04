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
        Events.TICK.post(Unit)
    }

    @SubscribeEvent
    fun onRender(event: net.minecraftforge.event.TickEvent.RenderTickEvent) {
        if (event.phase == net.minecraftforge.event.TickEvent.Phase.START) return
        Events.RENDER.post(UMatrixStack.Compat.get())
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onMouseClicked(mouseEvent: net.minecraftforge.client.event.GuiScreenEvent.MouseClickedEvent.Pre) {
        if (Events.MOUSE_CLICKED.post(mouseEvent.button)) {
            mouseEvent.isCanceled = true
        }
    }

    @SubscribeEvent
    fun onGuiClose(event: GuiOpenEvent) {
        Events.SCREEN_CHANGED.post(event.gui)
    }
}