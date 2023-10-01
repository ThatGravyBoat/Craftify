package tech.thatgravyboat.craftify.utils

import gg.essential.elementa.components.GradientComponent
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.constraints.CopyConstraintFloat
import gg.essential.elementa.constraints.HeightConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.BasicState
import gg.essential.elementa.utils.invisible
import gg.essential.elementa.utils.withAlpha
import gg.essential.vigilance.gui.VigilancePalette
import tech.thatgravyboat.craftify.ssl.FixSSL
import java.awt.Color
import java.awt.image.BufferedImage
import java.net.HttpURLConnection
import java.net.URL
import javax.imageio.ImageIO
import javax.net.ssl.HttpsURLConnection

object RenderUtils {

    fun getImage(url: URL): BufferedImage {
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.useCaches = true
        connection.addRequestProperty("User-Agent", "Mozilla/4.76 (Craftify)")
        connection.doOutput = true
        if (connection is HttpsURLConnection && FixSSL.context != null) {
            connection.sslSocketFactory = FixSSL.context!!.socketFactory
        }

        return ImageIO.read(connection.inputStream)
    }

    infix fun ScrollComponent.scrollGradient(heightSize: HeightConstraint) = apply {
        val topGradient = createScrollGradient(true, heightSize)
        createScrollGradient(false, 100.percent boundTo topGradient)
    }

    private fun ScrollComponent.createScrollGradient(
        top: Boolean,
        heightSize: HeightConstraint,
        color: Color = VigilancePalette.getTextShadow(),
        maxGradient: Int = 204,
    ): GradientComponent {

        val percentState = BasicState(0f)

        this.addScrollAdjustEvent(false) { percent, _ -> percentState.set(percent) }

        val gradient = object : GradientComponent(
            color.withAlpha(0),
            color.withAlpha(0)
        ) {
            // Override because the gradient should be treated as if it does not exist from an input point of view
            override fun isPointInside(x: Float, y: Float): Boolean {
                return false
            }
        }.bindStartColor(percentState.map {
            if (top) {
                color.withAlpha((it * 1000).toInt().coerceIn(0..maxGradient))
            } else {
                color.invisible()
            }
        }).bindEndColor(percentState.map {
            if (top) {
                color.invisible()
            } else {
                color.withAlpha(((1 - it) * 1000).toInt().coerceIn(0..maxGradient))
            }
        }).constrain {
            y = 0.pixels(alignOpposite = !top) boundTo this@createScrollGradient
            x = CopyConstraintFloat() boundTo this@createScrollGradient
            width = CopyConstraintFloat() boundTo this@createScrollGradient
            height = heightSize
        } childOf parent
        return gradient
    }
}