package tech.thatgravyboat.craftify.services.addons

import tech.thatgravyboat.jukebox.api.service.BaseService

interface Addon {

    fun setup(service: BaseService)
    fun close(service: BaseService)

    companion object {

        val addons = listOf<Addon>(
            PlayerUIAddon,
            ConfigAddon,

            ServerShareAddon,
            EssentialCompatAddon,
        )
    }
}