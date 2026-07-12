package tech.thatgravyboat.craftify.ui

import gg.essential.universal.UScreen
import net.minecraft.client.gui.GuiGraphicsExtractor
import tech.thatgravyboat.craftify.Craftify
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.platform.McClient
import tech.thatgravyboat.craftify.screens.PositionEditorScreen
import tech.thatgravyboat.craftify.themes.library.ScreenshotScreen
import tech.thatgravyboat.jukebox.api.state.State

object Player {

    var state: State? = null
        set(value) {
            field = value
            if (value != null) {
                UIPlayerV2.setState(value)
            }
        }

    private var temporarilyHidden = false
    val hidden: Boolean
        get() {
            if (temporarilyHidden) return true
            if (McClient.hideGui) return true
            if (Config.musicService == "disabled") return true
            if (UScreen.currentScreen is ScreenshotScreen) return false
            if (McClient.screen is PositionEditorScreen) return false
            if (!Config.renderType.canRender(McClient.screen)) return true
            if (!Config.displayMode.canDisplay(Craftify.service?.getState())) return true
            return false
        }

    fun toggleHiding() {
        temporarilyHidden = !temporarilyHidden
    }

    fun onRender(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (hidden) return
        UIPlayerV2.renderWithEffects(graphics, mouseX, mouseY, partialTicks)
    }

    fun onMouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return !this.hidden && UIPlayerV2.mouseClicked(mouseX, mouseY, button)
    }
}
