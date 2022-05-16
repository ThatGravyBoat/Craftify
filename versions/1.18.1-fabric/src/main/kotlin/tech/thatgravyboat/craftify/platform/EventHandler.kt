package tech.thatgravyboat.craftify.platform

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

object EventHandler {

    init {
        ClientTickEvents.START_CLIENT_TICK.register { eventBus.post(TickEvent()) }
    }
}