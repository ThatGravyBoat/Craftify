package tech.thatgravyboat.craftify.platform

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import tech.thatgravyboat.craftify.TickEvent
import tech.thatgravyboat.craftify.eventBus

object EventHandler {

    init {
        ClientTickEvents.START_CLIENT_TICK.register { eventBus.post(TickEvent()) }
    }
}