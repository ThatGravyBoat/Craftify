package tech.thatgravyboat.craftify.config

import gg.essential.vigilance.data.Migration
import tech.thatgravyboat.craftify.services.config.BeefwebServiceConfig
import tech.thatgravyboat.craftify.services.config.ServiceConfig
import tech.thatgravyboat.craftify.services.config.SpotifyServiceConfig
import tech.thatgravyboat.craftify.services.config.YoutubeServiceConfig

object ConfigMigrations {

    val migrations = listOf(
        // Updating Mod Mode from select to custom property
        Migration {
            it["general.music_service"] = when (it.remove("general.mod_mode") as? Int) {
                1 -> "spotify"
                2 -> "ytmd"
                3 -> "cider"
                4 -> "beefweb"
                else -> it["general.music_service"] ?: "disabled"
            }
        },

        // Migrate service configs to global system
        Migration {
            SpotifyServiceConfig.auth = it.remove("login.spotify_login_token") as? String ?: ""
            SpotifyServiceConfig.refresh = it.remove("login.refresh_token") as? String ?: ""
            YoutubeServiceConfig.token = it.remove("login.ytmd_token") as? String ?: ""
            BeefwebServiceConfig.port = it.remove("login.service_port") as? Int ?: 8880

            ServiceConfig.save()
        },

        // Removal of Essential announcement
        Migration {
            it["general.announcement"] = it.remove("general.announce_new_song") != 0
        },
    )
}