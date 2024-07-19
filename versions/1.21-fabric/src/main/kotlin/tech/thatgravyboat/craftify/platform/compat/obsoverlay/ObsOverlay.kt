package tech.thatgravyboat.craftify.platform.compat.obsoverlay

import net.fabricmc.loader.api.FabricLoader

object ObsOverlay {

    fun push() {
        me.zziger.obsoverlay.OverlayRenderer.beginDraw()
    }

    fun pop() {
        me.zziger.obsoverlay.OverlayRenderer.endDraw()
    }
}

object ObsOverlayCompat {

    private val enabled: Boolean = FabricLoader.getInstance().isModLoaded("obs-overlay")
    // TODO check and add config

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