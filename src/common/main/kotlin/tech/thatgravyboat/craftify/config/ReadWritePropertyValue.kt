package tech.thatgravyboat.craftify.config

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.PropertyValue
import kotlin.reflect.KMutableProperty0

class ReadWritePropertyValue(private val getter: () -> Any?, private val setter: (Any?) -> Unit) : PropertyValue() {
    override fun getValue(instance: Vigilant): Any? {
        return getter.invoke()
    }

    override fun setValue(value: Any?, instance: Vigilant) {
        setter.invoke(value)
    }

    companion object {

        inline fun <reified T> create(property: KMutableProperty0<T>): ReadWritePropertyValue {
            return ReadWritePropertyValue(
                getter = { property.get() },
                setter = {
                    if (it is T) {
                        property.set(it)
                    } else {
                        val name = it?.let { it::class.simpleName } ?: "null"
                        println("Invalid property type for ${property.name}: $name")
                    }
                }
            )
        }
    }
}