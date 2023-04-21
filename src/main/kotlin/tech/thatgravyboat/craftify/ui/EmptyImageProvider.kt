package tech.thatgravyboat.craftify.ui

import gg.essential.elementa.components.image.ImageProvider
import gg.essential.universal.UMatrixStack
import java.awt.Color

/**
 * An empty image provider that does nothing.
 * This is used to prevent the default image provider from drawing the default image.
 */
object EmptyImageProvider: ImageProvider {

    override fun drawImage(
        matrixStack: UMatrixStack,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        color: Color
    ) {
        // Do nothing
    }
}