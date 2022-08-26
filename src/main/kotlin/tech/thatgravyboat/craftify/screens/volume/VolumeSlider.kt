package tech.thatgravyboat.craftify.screens.volume

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.AspectConstraint
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.dsl.*
import gg.essential.universal.USound
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick
import java.awt.Color

/**
 * A copy of Slider from vigilance with an onValueChanged listener and changed design.
 */
class VolumeSlider(initialValue: Float) : UIContainer() {
    private var percentage = initialValue
    private var onValueChange: (Float) -> Unit = {}
    private var onValueChanged: (Float) -> Unit = {}
    private var dragging = false
    private var grabOffset = 0f

    private val outerBox = UIBlock(VigilancePalette.getBackground()).constrain {
        x = basicXConstraint {
            this@VolumeSlider.getLeft() + 1f + this@VolumeSlider.getHeight() * 0.75f
        }
        y = CenterConstraint()
        width = basicWidthConstraint {
            this@VolumeSlider.getWidth() - 2f - this@VolumeSlider.getHeight() * 1.5f
        }
        height = RelativeConstraint(0.5f)
    } childOf this

    private val completionBox = UIBlock().constrain {
        width = RelativeConstraint(percentage)
        height = RelativeConstraint(1f)
        color = VigilancePalette.getAccent().toConstraint()
    } childOf outerBox

    private val grabBox = UIBlock().constrain {
        x = basicXConstraint { completionBox.getRight() - it.getWidth() / 2f }
        y = CenterConstraint() boundTo outerBox
        width = AspectConstraint(1f)
        height = 100.percent()
        color = Color.WHITE.toConstraint()
    } childOf this

    init {
        grabBox.onLeftClick { event ->
            USound.playButtonPress()
            dragging = true
            grabOffset = event.relativeX - (grabBox.getWidth() / 2)
            event.stopPropagation()
        }.onMouseRelease {
            dragging = false
            grabOffset = 0f
            onValueChanged(percentage)
        }.onMouseDrag { mouseX, _, _ ->
            if (!dragging) return@onMouseDrag

            val clamped = (mouseX + grabBox.getLeft() - grabOffset).coerceIn(outerBox.getLeft()..outerBox.getRight())
            val percentage = (clamped - outerBox.getLeft()) / outerBox.getWidth()
            setCurrentPercentage(percentage)
        }

        outerBox.onLeftClick { event ->
            USound.playButtonPress()
            val percentage = event.relativeX / outerBox.getWidth()
            setCurrentPercentage(percentage)
            dragging = true
        }
    }

    fun getCurrentPercentage() = percentage

    fun setCurrentPercentage(newPercentage: Float, callListener: Boolean = true) {
        percentage = newPercentage.coerceIn(0f..1f)

        completionBox.setWidth(RelativeConstraint(percentage))

        if (callListener) {
            onValueChange(percentage)
        }
    }

    fun onValueChange(listener: (Float) -> Unit) {
        onValueChange = listener
    }

    fun onValueChanged(listener: (Float) -> Unit) {
        onValueChanged = listener
    }
}