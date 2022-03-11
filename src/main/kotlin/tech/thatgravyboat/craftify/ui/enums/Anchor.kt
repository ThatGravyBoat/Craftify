package tech.thatgravyboat.craftify.ui.enums

import gg.essential.elementa.UIComponent
import gg.essential.elementa.constraints.PositionConstraint
import gg.essential.universal.UResolution
import tech.thatgravyboat.craftify.Config

enum class Anchor(private val xAlignment: Alignment, private val yAlignment: Alignment) {
    TOPLEFT(Alignment.LEFT, Alignment.TOP),
    TOPMIDDLE(Alignment.MIDDLE, Alignment.TOP),
    TOPRIGHT(Alignment.RIGHT, Alignment.TOP),
    MIDDLELEFT(Alignment.LEFT, Alignment.MIDDLE),
    MIDDLERIGHT(Alignment.RIGHT, Alignment.MIDDLE),
    BOTTOMLEFT(Alignment.LEFT, Alignment.BOTTOM),
    BOTTOMMIDDLE(Alignment.MIDDLE, Alignment.BOTTOM),
    BOTTOMRIGHT(Alignment.RIGHT, Alignment.BOTTOM);

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
}
