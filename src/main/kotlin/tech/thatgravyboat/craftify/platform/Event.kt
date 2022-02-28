package tech.thatgravyboat.craftify.platform

class Event {

    private var cancelled = false

    fun cancel() {
        cancelled = true
    }

    fun isCancelled() = cancelled
}
