package tech.thatgravyboat.craftify.ui.constraints

import gg.essential.elementa.UIComponent
import gg.essential.elementa.constraints.ConstraintType
import gg.essential.elementa.constraints.resolution.ConstraintVisitor
import gg.essential.elementa.font.DefaultFonts
import gg.essential.elementa.font.ElementaFonts
import gg.essential.elementa.font.FontProvider
import gg.essential.universal.UMatrixStack
import tech.thatgravyboat.craftify.themes.ThemeConfig
import java.awt.Color

class ThemeFontProvider(private val id: String) : FontProvider {

    override var cachedValue: FontProvider = this
    override var recalculate: Boolean = false
    override var constrainTo: UIComponent? = null

    private fun getFont(): FontProvider {
        val font = when (id) {
            "artist" -> ThemeConfig.artistFont
            "title" -> ThemeConfig.titleFont
            "progress" -> ThemeConfig.progressFont
            else -> 0
        }
        return when (font) {
            1 -> ElementaFonts.MINECRAFT
            2 -> ElementaFonts.MINECRAFT_BOLD
            3 -> ElementaFonts.JETBRAINS_MONO
            4 -> ElementaFonts.MINECRAFT_FIVE
            else -> DefaultFonts.VANILLA_FONT_RENDERER
        }
    }

    override fun getBaseLineHeight() = getFont().getBaseLineHeight()
    override fun getBelowLineHeight() = getFont().getBelowLineHeight()
    override fun getShadowHeight() = getFont().getShadowHeight()
    override fun getStringHeight(string: String, pointSize: Float) = getFont().getStringHeight(string, pointSize)
    override fun getStringWidth(string: String, pointSize: Float) = getFont().getStringWidth(string, pointSize)
    override fun drawString(
        matrixStack: UMatrixStack,
        string: String,
        color: Color,
        x: Float,
        y: Float,
        originalPointSize: Float,
        scale: Float,
        shadow: Boolean,
        shadowColor: Color?
    ) {
        getFont().drawString(matrixStack, string, color, x, y, originalPointSize, scale, shadow, shadowColor)
    }

    @Deprecated(
        "For 1.17 this method requires you pass a UMatrixStack as the first argument.\n\nIf you are currently extending this method, you should instead extend the method with the added argument.\nNote however for this to be non-breaking, your parent class needs to transition before you do.\n\nIf you are calling this method and you cannot guarantee that your target class has been fully updated (such as when\ncalling an open method on an open class), you should instead call the method with the \"Compat\" suffix, which will\ncall both methods, the new and the deprecated one.\nIf you are sure that your target class has been updated (such as when calling the super method), you should\n(for super calls you must!) instead just call the method with the original name and added argument.",
        replaceWith = ReplaceWith("drawString(matrixStack, string, color, x, y, originalPointSize, scale, shadow, shadowColor)")
    )
    override fun drawString(
        string: String,
        color: Color,
        x: Float,
        y: Float,
        originalPointSize: Float,
        scale: Float,
        shadow: Boolean,
        shadowColor: Color?
    ) {
        getFont().drawString(string, color, x, y, originalPointSize, scale, shadow, shadowColor)
    }

    override fun visitImpl(visitor: ConstraintVisitor, type: ConstraintType) {
        getFont().visitImpl(visitor, type)
    }

}