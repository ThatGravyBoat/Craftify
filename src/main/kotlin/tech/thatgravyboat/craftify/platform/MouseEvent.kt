package tech.thatgravyboat.craftify.platform

class MouseEvent(val button: Int) {

    private var cancelled = false

    fun cancel() {
        cancelled = true
    }

    fun isCancelled() = cancelled
}
