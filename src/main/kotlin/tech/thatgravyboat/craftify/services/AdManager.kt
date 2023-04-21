package tech.thatgravyboat.craftify.services

import tech.thatgravyboat.jukebox.api.state.PlayingType
import tech.thatgravyboat.jukebox.api.state.Song
import tech.thatgravyboat.jukebox.api.state.SongState
import tech.thatgravyboat.jukebox.api.state.State

object AdManager {

    private val ads: Map<String, (State) -> State> = mapOf(
        "bisect" to {
            State(
                it.player,
                Song(
                    "Bisect Hosting Partner",
                    listOf("use code '§agravy§r'"),
                    "https://www.bisecthosting.com/images/logos/logo-app.png",
                    "https://www.bisecthosting.com/gravy",
                    PlayingType.AD
                ),
                SongState(0, 0, false)
            )
        }
    )

    private var adId: String? = null

    fun changeAd() {
        adId = null
    }

    private fun getAd(): (State) -> State {
        if (adId == null) {
            adId = ads.keys.random()
        }
        return ads[adId]!!
    }

    fun getAdState(state: State): State {
        return getAd()(state)
    }

}