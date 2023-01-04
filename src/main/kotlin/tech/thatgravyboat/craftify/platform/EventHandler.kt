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
        Events.TICK.post(Unit)
    }

    @SubscribeEvent
    fun onRender(event: net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent) {
        if (event.phase == net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START) return
        Events.RENDER.post(UMatrixStack.Compat.get())
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onMouseClicked(mouseEvent: net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent.Pre) {
        val shouldContinue = org.lwjgl.input.Mouse.getEventButtonState()
        val button = org.lwjgl.input.Mouse.getEventButton()
        if (shouldContinue && Events.MOUSE_CLICKED.post(button)) {
            mouseEvent.isCanceled = true
        }
    }

    @SubscribeEvent
    fun onGuiClose(event: GuiOpenEvent) {
        Events.SCREEN_CHANGED.post(event.gui)
    }
}