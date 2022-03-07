package tech.thatgravyboat.craftify.ui

import gg.essential.api.utils.Multithreading
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import tech.thatgravyboat.craftify.api.SpotifyAPI
import tech.thatgravyboat.craftify.themes.ThemeConfig
import tech.thatgravyboat.craftify.ui.constraints.ConfigColorConstraint
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.floor

class UIProgressBar : UIRoundedRectangle(ThemeConfig.progressRadius) {

    private var stop: Boolean = false
    private var timer: AtomicInteger = AtomicInteger(0)
    private val timerUpdater: ScheduledFuture<*>
    private var end: Int = 1

    init {
        constrain {
            color = ConfigColorConstraint("progress_background")
        }

        timerUpdater = Multithreading.schedule({
            if (!stop && SpotifyAPI.lastState?.is_playing == true && timer.get() < end) {
                timer.incrementAndGet()
                update()
            }
        }, 1, 1, TimeUnit.SECONDS)
    }

    private val bar = UIRoundedRectangle(ThemeConfig.progressRadius).constrain {
        width = 0.percent()
        height = 100.percent()
        color = ConfigColorConstraint("progress_bar")
    } childOf this

    private val startTime = UIText("0:00").constrain {
        y = (-6).pixel()
        textScale = 0.5.pixel()
        color = ConfigColorConstraint("progress_text")
    } childOf this

    private val endTime = UIText("0:00").let {
        it.constrain {
            x = 100.percent() - "0:00".width(0.5f).pixel()
            y = (-6).pixel()
            color = ConfigColorConstraint("progress_text")

            textScale = 0.5.pixel()
        } childOf this
    }

    fun tempStop() {
        stop = true
    }

    fun updateTime(start: Int, end: Int) {
        stop = false
        timer.set(start)
        this.end = end
        update()
    }

    private fun update() {
        val start = timer.get()
        if (start == 0) {
            bar.setWidth(0.percent())
        } else {
            bar.animate {
                val newWidth = ((start / end.toDouble()) * 100).percent()
                val newWidthValue = newWidth.getWidth(bar)
                setWidthAnimation(Animations.LINEAR, if (this.width.getWidth(bar) > newWidthValue) 0f else 1f, ((start / end.toDouble()) * 100).percent())
            }
        }
        startTime.setText("${floor((start / 60).toDouble()).toInt()}:${(start % 60).let { if (it < 10) "0$it" else "$it" }}")
        endTime.setText("${floor((end / 60).toDouble()).toInt()}:${(end % 60).let { if (it < 10) "0$it" else "$it" }}")
        endTime.constraints.x = endTime.getText().width(0.5f).pixel(alignOpposite = true) + endTime.getText().width(0.5f).pixel()
    }

    fun updateTheme() {
        bar.setRadius(ThemeConfig.progressRadius.pixels())
        this.setRadius(ThemeConfig.progressRadius.pixels())
    }
}
