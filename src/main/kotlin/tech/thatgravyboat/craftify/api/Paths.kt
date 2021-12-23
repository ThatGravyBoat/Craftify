package tech.thatgravyboat.craftify.api

enum class Paths(val url: String, val methodType: MethodType, val requiresBody: Boolean = false, val requireParams: Boolean = false) {
    PLAYER("https://api.spotify.com/v1/me/player", MethodType.GET),
    PLAY("https://api.spotify.com/v1/me/player/play", MethodType.PUT, true),
    PAUSE("https://api.spotify.com/v1/me/player/pause", MethodType.PUT, true),
    SHUFFLE("https://api.spotify.com/v1/me/player/shuffle", MethodType.PUT, true, true),
    REPEAT("https://api.spotify.com/v1/me/player/repeat", MethodType.PUT, true, true),
    NEXT("https://api.spotify.com/v1/me/player/next", MethodType.POST, true),
    PREV("https://api.spotify.com/v1/me/player/previous", MethodType.POST, true),
}
