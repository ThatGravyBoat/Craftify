package tech.thatgravyboat.craftify

import net.fabricmc.api.ClientModInitializer

object Craftify : ClientModInitializer {

    override fun onInitializeClient() {
        Initializer.init()
    }
}