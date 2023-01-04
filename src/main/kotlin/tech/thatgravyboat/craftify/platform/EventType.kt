package tech.thatgravyboat.craftify.platform

class EventType<T> {

    private val listeners = mutableListOf<(T) -> Unit>()

    fun register(listener: (T) -> Unit) {
        listeners.add(listener)
    }

    fun post(event: T) {
        listeners.forEach { it(event) }
    }

}