package tech.thatgravyboat.craftify.platform.compat.obsoverlay

object ObsOverlayCompat {

    val enabled: Boolean
        get() = false

    fun draw(action: () -> Unit) {
        action()
    }
}