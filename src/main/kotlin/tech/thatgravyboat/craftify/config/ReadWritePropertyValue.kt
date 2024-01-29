package tech.thatgravyboat.craftify.config

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.PropertyValue

class ReadWritePropertyValue(private val getter: () -> Any?, private val setter: (Any?) -> Unit) : PropertyValue() {
    override fun getValue(instance: Vigilant): Any? {
        return getter.invoke()
    }

    override fun setValue(value: Any?, instance: Vigilant) {
        setter.invoke(value)
    }
}