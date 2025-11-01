package tech.thatgravyboat.craftify.services

import gg.essential.universal.UDesktop
import gg.essential.vigilance.data.CategoryItem
import tech.thatgravyboat.craftify.config.ServiceConfigs
import tech.thatgravyboat.craftify.services.config.BeefwebServiceConfig
import tech.thatgravyboat.craftify.services.config.SpotifyServiceConfig
import tech.thatgravyboat.craftify.services.config.YoutubeServiceConfig
import tech.thatgravyboat.craftify.utils.Utils
import tech.thatgravyboat.jukebox.api.service.BaseService
import tech.thatgravyboat.jukebox.impl.applescript.AppleMusicService
import tech.thatgravyboat.jukebox.impl.foobar.FoobarService
import tech.thatgravyboat.jukebox.impl.mpd.MpdService
import tech.thatgravyboat.jukebox.impl.spotify.SpotifyService
import tech.thatgravyboat.jukebox.impl.tidal.TidalService
import tech.thatgravyboat.jukebox.impl.youtubev2.YoutubeServiceV2
import kotlin.reflect.KClass
import tech.thatgravyboat.jukebox.impl.apple.AppleService as CiderV1Service
import tech.thatgravyboat.jukebox.impl.cider.CiderService as CiderV2Service

class ServiceType<T : BaseService>(
    val type: KClass<T>,
    val id: String,
    val name: String,
    val config: List<CategoryItem>,
    val isAvailable: Boolean = true,
    val factory: () -> T
) {

    fun create(): T = factory()
    fun createOrReuse(existing: BaseService?): T = if (existing == null || existing::class != type) {
        create()
    } else {
        existing as T
    }
}

object ServiceTypes {

    val SERVICES = listOf(
        create<SpotifyService>("spotify", "Spotify", ServiceConfigs.SPOTIFY, true) {
            SpotifyService(SpotifyServiceConfig.auth).also(ServiceHelper::setupSpotify)
        },

        create<YoutubeServiceV2>("ytmd", "YT Music Desktop App", ServiceConfigs.YOUTUBE, true) {
            YoutubeServiceV2(YoutubeServiceConfig.token)
        },

        create<CiderV1Service>("cider", "Cider (Apple Music)", ServiceConfigs.CIDER, true, ::CiderV1Service),
        create<CiderV2Service>("cider2", "Cider 2 (Apple Music)", ServiceConfigs.CIDER2, true, ::CiderV2Service),
        create<AppleMusicService>("applescript", "Apple Music (MacOS)", ServiceConfigs.APPLESCRIPT, UDesktop.isMac) {
            AppleMusicService { command -> Utils.execCommand("osascript", "-e", command).getOrNull() }
        },

        create<TidalService>("tidal", "Tidal-HiFi", ServiceConfigs.TIDAL, true, ::TidalService),

        create<MpdService>("mpd", "Music Player Daemon", ServiceConfigs.MPD, true) { MpdService(BeefwebServiceConfig.port) },
        create<FoobarService>("beefweb", "Beefweb (Foobar2000 & DeaDBeeF)", ServiceConfigs.BEEFWEB, true) { FoobarService(BeefwebServiceConfig.port, true) },
    )

    fun fromId(id: String?): ServiceType<out BaseService>? = SERVICES.find { it.id == id }

    private inline fun <reified T : BaseService> create(
        id: String,
        name: String,
        config: List<CategoryItem>,
        isAvailable: Boolean,
        noinline factory: () -> T,
    ): ServiceType<T> {
        return ServiceType(T::class, id, name, config, isAvailable, factory)
    }
}