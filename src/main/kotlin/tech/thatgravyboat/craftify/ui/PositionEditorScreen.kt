package tech.thatgravyboat.craftify.ui

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.universal.UResolution
import gg.essential.vigilance.gui.VigilancePalette
import tech.thatgravyboat.craftify.config.Config
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.ui.enums.Anchor

class PositionEditorScreen : WindowScreen(ElementaVersion.V2) {

    private var clickPos: Pair<Float, Float>? = null

    private val box = UIBlock(VigilancePalette.getHighlight()).constrain {
        height = 50.pixels()
        width = if (ThemeConfig.hideImage) 105.pixels() else 150.pixels()
    }.apply {
        this.setX(Anchor.values()[Config.anchorPoint].getX(this))
        this.setY(Anchor.values()[Config.anchorPoint].getY(this))
    }.onMouseClick {
        if (it.relativeX < 0 || it.relativeY < 0 || it.relativeX > getWidth() || it.relativeY > getHeight()) {
            clickPos = null
        } else {
            clickPos = it.relativeX to it.relativeY
        }
    }.onMouseRelease {
        clickPos = null
    }.onMouseDrag { mouseX, mouseY, button ->
        if (clickPos == null) {
            return@onMouseDrag
        }

        if (button == 0) {
            this@onMouseDrag.constrain {
                x = ((this@onMouseDrag.getLeft() + mouseX - clickPos!!.first).coerceIn(0f, UResolution.scaledWidth - if (ThemeConfig.hideImage) 105f else 150f)).pixels()
                y = ((this@onMouseDrag.getTop() + mouseY - clickPos!!.second).coerceIn(0f, UResolution.scaledHeight - 50f)).pixels()
            }
        }
    } childOf this.window

    override fun onScreenClose() {
        super.onScreenClose()

        val position = Anchor.values()[Config.anchorPoint]
        Config.xOffset = position.getXOffset(box)
        Config.yOffset = position.getYOffset(box)
        Config.markDirty()
        Config.writeData()
        Player.changePosition(position)
    }
}
