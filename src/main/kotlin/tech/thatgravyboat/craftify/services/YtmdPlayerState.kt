package tech.thatgravyboat.craftify.services

import com.google.gson.JsonObject
import tech.thatgravyboat.craftify.types.*

object YtmdPlayerState {

    fun parseState(json: JsonObject): PlayerState {
        val player = json.getAsJsonObject("player")
        val track = json.getAsJsonObject("track")
        val repeating: Boolean = player.has("repeatType") && player.get("repeatType").asString != "NONE"
        val duration = track.get("duration").asLong * 1000
        val progress = (duration * player.get("statePercent").asDouble).toLong()

        val artist = Artist(track.get("author").asString)
        val art = AlbumImage(1, 1, if (track.get("isAdvertisement").asBoolean) "https://i.imgur.com/TKMuP2R.png" else track.get("cover").asString)
        val album = Album(listOf(art), track.get("url").asString)
        val urls = ExternalUrls(track.get("url").asString)
        val item = PlayerItem(duration, track.get("title").asString, listOf(artist), album, urls)
        val device = DeviceData(player.get("volumePercent").asInt)


        return PlayerState(
            false,
            if (repeating) "on" else "off",
            progress,
            player.get("hasSong").asBoolean && !player.get("isPaused").asBoolean,
            item,
            device
        )
    }

}