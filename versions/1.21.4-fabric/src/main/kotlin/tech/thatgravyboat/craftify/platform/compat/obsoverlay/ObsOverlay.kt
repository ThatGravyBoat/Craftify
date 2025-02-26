package tech.thatgravyboat.craftify.platform.compat.obsoverlay

import net.fabricmc.loader.api.FabricLoader
import tech.thatgravyboat.craftify.config.Config

object ObsOverlay {

    fun push() {
        me.zziger.obsoverlay.OverlayRenderer.beginDraw()
    }

    fun pop() {
        me.zziger.obsoverlay.OverlayRenderer.endDraw()
    }
}

object ObsOverlayCompat {

    private val isLoaded = FabricLoader.getInstance().isModLoaded("obs-overlay")
    val enabled: Boolean
        get() = isLoaded && Config.streamerMode

    fun draw(action: () -> Unit) {
        if (enabled) {
            ObsOverlay.push()
            action()
            ObsOverlay.pop()
        } else {
            action()
        }
    }
}