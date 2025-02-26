package tech.thatgravyboat.craftify.platform

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

object EventHandler {

    init {
        ClientTickEvents.END_CLIENT_TICK.register {
            Events.TICK.post(Unit)
        }
    }
}