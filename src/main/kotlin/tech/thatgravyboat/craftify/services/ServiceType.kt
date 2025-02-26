package tech.thatgravyboat.craftify.services

import tech.thatgravyboat.craftify.services.ServiceType.values
import tech.thatgravyboat.craftify.services.config.BeefwebServiceConfig
import tech.thatgravyboat.craftify.services.config.SpotifyServiceConfig
import tech.thatgravyboat.craftify.services.config.YoutubeServiceConfig
import tech.thatgravyboat.jukebox.api.service.BaseService
import tech.thatgravyboat.jukebox.impl.apple.AppleService
import tech.thatgravyboat.jukebox.impl.cider.CiderService
import tech.thatgravyboat.jukebox.impl.foobar.FoobarService
import tech.thatgravyboat.jukebox.impl.spotify.SpotifyService
import tech.thatgravyboat.jukebox.impl.tidal.TidalService
import tech.thatgravyboat.jukebox.impl.youtube.YoutubeService
import tech.thatgravyboat.jukebox.impl.youtubev2.YoutubeServiceV2
import kotlin.reflect.KClass

enum class ServiceType(val type: KClass<out BaseService>, val id: String, val factory: () -> BaseService) {
    CIDER(AppleService::class, "cider", { AppleService() }),
    CIDER2(CiderService::class, "cider2", { CiderService() }),
    YOUTUBE(YoutubeService::class, "ytmd", { YoutubeServiceV2(YoutubeServiceConfig.token) }),
    SPOTIFY(SpotifyService::class, "spotify", { SpotifyService(SpotifyServiceConfig.auth).also(ServiceHelper::setupSpotify) }),
    BEEFWEB(FoobarService::class, "beefweb", { FoobarService(BeefwebServiceConfig.port, true) }),
    TIDAL(TidalService::class, "tidal", { TidalService() }),
    ;

    fun create(): BaseService = factory()

    companion object {
        fun fromId(id: String): ServiceType? = values().find { it.id == id }
    }
}