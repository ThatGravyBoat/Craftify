package tech.thatgravyboat.craftify.ui.enums

import gg.essential.elementa.UIComponent
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.PositionConstraint
import gg.essential.elementa.dsl.minus
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.plus

enum class Alignment(val constraint: UIComponent.(offset: Float) -> PositionConstraint) {
    TOP({ offset -> offset.percent() }),
    LEFT({ offset -> offset.percent() }),
    MIDDLE({ offset -> CenterConstraint() + offset.percent() }),
    RIGHT({ offset -> offset.percent() - this.getWidth().pixels() }),
    BOTTOM({ offset -> offset.percent() - this.getHeight().pixels() })
}
