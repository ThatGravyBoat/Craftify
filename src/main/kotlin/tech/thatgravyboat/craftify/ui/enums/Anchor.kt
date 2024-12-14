package tech.thatgravyboat.craftify.ui.enums

import gg.essential.elementa.UIComponent
import gg.essential.elementa.constraints.PositionConstraint
import gg.essential.universal.UResolution
import tech.thatgravyboat.craftify.config.Config

enum class Anchor(private val xAlignment: Alignment, private val yAlignment: Alignment) {
    TOP_LEFT(Alignment.LEFT, Alignment.TOP),
    TOP_MIDDLE(Alignment.MIDDLE, Alignment.TOP),
    TOP_RIGHT(Alignment.RIGHT, Alignment.TOP),
    MIDDLE_LEFT(Alignment.LEFT, Alignment.MIDDLE),
    MIDDLE_RIGHT(Alignment.RIGHT, Alignment.MIDDLE),
    BOTTOM_LEFT(Alignment.LEFT, Alignment.BOTTOM),
    BOTTOM_MIDDLE(Alignment.MIDDLE, Alignment.BOTTOM),
    BOTTOM_RIGHT(Alignment.RIGHT, Alignment.BOTTOM);

    fun getDefaultXOffset(): Float {
        return if (xAlignment == Alignment.RIGHT) 1f else 0f
    }

    fun getDefaultYOffset(): Float {
        return if (yAlignment == Alignment.BOTTOM) 1f else 0f
    }

    fun getX(constraint: UIComponent): PositionConstraint {
        return this.xAlignment.constraint(constraint, Config.xOffset * 100)
    }

    fun getY(constraint: UIComponent): PositionConstraint {
        return this.yAlignment.constraint(constraint, Config.yOffset * 100)
    }

    fun getXOffset(component: UIComponent): Float {
        return when (xAlignment) {
            Alignment.LEFT -> component.getLeft() / UResolution.scaledWidth
            Alignment.MIDDLE -> ((component.getLeft() + (component.getWidth() / 2)) - (UResolution.scaledWidth / 2)) / UResolution.scaledWidth
            Alignment.RIGHT -> component.getRight() / UResolution.scaledWidth
            else -> 0f
        }
    }

    fun getYOffset(component: UIComponent): Float {
        return when (yAlignment) {
            Alignment.TOP -> component.getTop() / UResolution.scaledHeight
            Alignment.MIDDLE -> ((component.getTop() + (component.getHeight() / 2)) - (UResolution.scaledHeight / 2)) / UResolution.scaledHeight
            Alignment.BOTTOM -> component.getBottom() / UResolution.scaledHeight
            else -> 0f
        }
    }

    override fun toString(): String =
        this.name.split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
}
