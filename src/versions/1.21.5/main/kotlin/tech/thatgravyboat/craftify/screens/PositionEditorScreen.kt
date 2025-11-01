package tech.thatgravyboat.craftify.screens

import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.ui.UIPlayerV2

actual fun PositionEditorScreen(): Screen = PositionEditorScreenImpl()
actual fun Screen.isPositionEditor(): Boolean = this is PositionEditorScreenImpl

class PositionEditorScreenImpl : Screen(Component.empty()) {

    private var dragging = false
    private var startDifferenceX = 0
    private var startDifferenceY = 0

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        startDifferenceX = mouseX.toInt() - Config.anchorPoint.getX(UIPlayerV2.width, Config.xOffset)
        startDifferenceY = mouseY.toInt() - Config.anchorPoint.getY(UIPlayerV2.height, Config.yOffset)
        dragging = startDifferenceX in 0..UIPlayerV2.width && startDifferenceY in 0..UIPlayerV2.height
        return true
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        if (dragging) {
            Config.xOffset = Config.anchorPoint.getXOffset(UIPlayerV2.width, (mouseX - startDifferenceX).toInt())
            Config.yOffset = Config.anchorPoint.getYOffset(UIPlayerV2.height, (mouseY - startDifferenceY).toInt())
            return true
        }
        return false
    }
}