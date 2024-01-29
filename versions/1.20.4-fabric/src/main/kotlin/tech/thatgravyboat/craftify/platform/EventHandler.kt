package tech.thatgravyboat.craftify.platform

import gg.essential.universal.UMatrixStack
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback

object EventHandler {

    init {
        ClientTickEvents.END_CLIENT_TICK.register {
            Events.TICK.post(Unit)
        }
        HudRenderCallback.EVENT.register(HudRenderCallback { context, _ ->
            Events.RENDER.post(UMatrixStack(context.matrices))
        })
    }
}