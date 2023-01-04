package tech.thatgravyboat.craftify.platform

class CancellableEventType<T> {

    private val listeners = mutableListOf<(T) -> Boolean>()

    fun register(listener: (T) -> Boolean) {
        listeners.add(listener)
    }

    fun post(event: T): Boolean {
        var cancelled = false
        listeners.forEach {
            if (it(event)) {
                cancelled = true
            }
        }
        return cancelled
    }

}