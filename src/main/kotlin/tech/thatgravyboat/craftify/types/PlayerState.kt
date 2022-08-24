package tech.thatgravyboat.craftify.types

import kotlin.math.roundToInt

data class ExternalUrls(val spotify: String)

data class PlayerItem(
    val duration_ms: Long,
    val name: String,
    val artists: List<Artist>,
    val album: Album,
    val external_urls: ExternalUrls
)

data class Artist(
    val name: String
)

data class Album(
    val images: List<AlbumImage>,
    val uri: String
)

data class AlbumImage(
    val height: Int,
    val width: Int,
    val url: String
)

data class DeviceData(
    val volume_percent: Int
)

data class PlayerState(
    val shuffle_state: Boolean,
    val repeat_state: String,
    val progress_ms: Long,
    val is_playing: Boolean,
    val item: PlayerItem?,
    val device: DeviceData?,
    val currently_playing_type: String?
) {

    fun hasData() = item != null

    fun getTitle() = item?.name ?: "Media Not Found"

    fun getTime() = (progress_ms / 1000).toDouble().roundToInt()

    fun getEndTime() = ((item?.duration_ms ?: 0) / 1000).toDouble().roundToInt()

    fun getArtists() = item?.artists?.joinToString(", ") { artist -> artist.name } ?: "Possible Error Occurred."

    fun isRepeating() = "off" != repeat_state

    fun isShuffling() = shuffle_state

    fun isPlaying() = is_playing

    fun getImage() = item?.album?.images?.sortedWith(compareByDescending { it.width })?.first()?.url ?: "https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/26d4.png"

    fun isAd() = currently_playing_type == "ad"
}
