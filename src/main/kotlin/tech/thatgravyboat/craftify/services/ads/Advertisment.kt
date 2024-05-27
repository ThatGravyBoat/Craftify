package tech.thatgravyboat.craftify.services.ads

import tech.thatgravyboat.jukebox.api.state.PlayingType
import tech.thatgravyboat.jukebox.api.state.Song
import tech.thatgravyboat.jukebox.api.state.SongState
import tech.thatgravyboat.jukebox.api.state.State

data class Advertisment(
    val title: String,
    val subtitle: String,
    val image: String,
    val link: String
) {

    fun toState(state: State): State {
        return State(
            state.player,
            Song(this.title, listOf(this.subtitle), this.image, this.link, PlayingType.AD),
            SongState(0, 0, false)
        )
    }
}
