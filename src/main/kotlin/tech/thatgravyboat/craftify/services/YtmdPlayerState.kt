package tech.thatgravyboat.craftify.services

import com.google.gson.JsonObject
import tech.thatgravyboat.craftify.extensions.*
import tech.thatgravyboat.craftify.types.*

object YtmdPlayerState {

    fun parseState(json: JsonObject): PlayerState {
        val player = json.getAsJsonObject("player")
        val track = json.getAsJsonObject("track")
        val repeating: Boolean = player.getAsString("repeatType", "NONE") != "NONE"
        val duration = track.getAsLong("duration") * 1000
        val progress = (duration * player.getAsDouble("statePercent")).toLong()

        val artist = Artist(track.getAsString("author", "Possible Error Occurred."))
        val art = AlbumImage(1, 1, track.getAsString("cover", "https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/26d4.png"))
        val album = Album(listOf(art), track.getAsString("url"))
        val urls = ExternalUrls(track.getAsString("url"))
        val item = PlayerItem(duration, track.getAsString("title", "Media Not Found"), listOf(artist), album, urls)
        val device = DeviceData(track.getAsInt("volumePercent", 100))

        return PlayerState(
            false,
            if (repeating) "on" else "off",
            progress,
            track.getAsBoolean("hasSong", true) && !track.getAsBoolean("isPaused"),
            item,
            device,
            if (track.getAsBoolean("isAdvertisement", false)) "ad" else "track"
        )
    }

}