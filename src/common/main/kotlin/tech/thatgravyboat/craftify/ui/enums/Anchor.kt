package tech.thatgravyboat.craftify.ui.enums

import net.minecraft.util.Mth
import tech.thatgravyboat.craftify.platform.McClient

enum class Anchor(private val xAlignment: Alignment, private val yAlignment: Alignment) {
    TOP_LEFT(Alignment.LEFT, Alignment.TOP),
    TOP_MIDDLE(Alignment.MIDDLE, Alignment.TOP),
    TOP_RIGHT(Alignment.RIGHT, Alignment.TOP),
    MIDDLE_LEFT(Alignment.LEFT, Alignment.MIDDLE),
    MIDDLE_RIGHT(Alignment.RIGHT, Alignment.MIDDLE),
    BOTTOM_LEFT(Alignment.LEFT, Alignment.BOTTOM),
    BOTTOM_MIDDLE(Alignment.MIDDLE, Alignment.BOTTOM),
    BOTTOM_RIGHT(Alignment.RIGHT, Alignment.BOTTOM);

    fun getX(width: Int, offset: Int): Int {
        val x = this.xAlignment.calculator.invoke(McClient.window.guiScaledWidth, width, offset)
        return Mth.clamp(x, 0, McClient.window.guiScaledWidth - width)
    }

    fun getY(height: Int, offset: Int): Int {
        val y = this.yAlignment.calculator.invoke(McClient.window.guiScaledHeight, height, offset)
        return Mth.clamp(y, 0, McClient.window.guiScaledHeight - height)
    }

    fun getXOffset(width: Int, mouseX: Int): Int {
        return this.xAlignment.positioner.invoke(McClient.window.guiScaledWidth, width, mouseX)
    }

    fun getYOffset(height: Int, mouseY: Int): Int {
        return this.yAlignment.positioner.invoke(McClient.window.guiScaledHeight, height, mouseY)
    }

    override fun toString(): String =
        this.name.split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
}
