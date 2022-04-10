package tech.thatgravyboat.craftify.ui

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.Window
import gg.essential.elementa.dsl.width
import gg.essential.elementa.state.BasicState
import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack

open class UITextMarquee(private val frames: Float = .5f, text: String) : UIComponent() {
    private val textState = BasicState("$text   ").map { it }
    private var ogText = text
    private var shouldAnimate = text.width() > this.getWidth()
    private var elapsedFrames = 0
    private var totalFrames = 0

    private fun getText() = textState.get()
    private fun setText(text: String) = apply { textState.set(text) }

    private fun shouldUpdate() = elapsedFrames >= totalFrames

    override fun animationFrame() {
        super.animationFrame()
        if (shouldAnimate) {
            elapsedFrames++

            if (shouldUpdate()) {
                val newText = this.getText()
                this.setText("${newText.drop(1)}${newText.take(1)}")
                elapsedFrames = 0
                totalFrames = (frames * Window.of(this).animationFPS).toInt()
            }
        }
    }

    override fun draw(matrixStack: UMatrixStack) {
        val text = textState.get()
        if (text.isEmpty()) return

        beforeDraw(matrixStack)

        val x = getLeft()
        val y = getTop()
        val color = getColor()

        if (color.alpha <= 10)
            return super.draw(matrixStack)

        UGraphics.enableBlend()

        val formattedText = splitText(text)

        getFontProvider().drawString(
            matrixStack, formattedText, color, x, y,
            10f, 1.0f, true, null
        )
        super.draw(matrixStack)
    }

    fun updateText(text: String) {
        if (text != ogText) {
            this.setText("$text   ")
            ogText = text
        }
        shouldAnimate = text.width() > this.getWidth()
    }

    private fun splitText(text: String): String {
        if (text.width() <= this.getWidth()) return text
        var currWidth = 0.0
        var index = 0

        while (index < text.length) {
            val charWidth = text[index].width()
            if (currWidth + charWidth > this.getWidth()) break
            currWidth += charWidth
            index++
        }
        return text.substring(0, index)
    }
}
