package tech.thatgravyboat.craftify.config

import gg.essential.elementa.UIComponent
import gg.essential.elementa.constraints.ColorConstraint
import gg.essential.elementa.constraints.ConstraintType
import gg.essential.elementa.constraints.resolution.ConstraintVisitor
import java.awt.Color

class SuppliedColoredConstraint(private val colorGetter: () -> Color) : ColorConstraint {
    override var cachedValue: Color = Color.WHITE
    override var recalculate = true

    override fun getColorImpl(component: UIComponent): Color {
        return colorGetter.invoke()
    }

    override fun to(component: UIComponent) = apply {
        throw UnsupportedOperationException("Constraint.to(UIComponent) is not available in this context!")
    }

    override fun visitImpl(visitor: ConstraintVisitor, type: ConstraintType) {}

    override var constrainTo: UIComponent? = null
}
