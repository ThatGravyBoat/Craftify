package tech.thatgravyboat.craftify.platform

class UKeybind(private val name: String, private val category: String, private val type: Type, private val code: Int) {

    private lateinit var bind: MCKeyBinding

    fun getBinding() = bind

    fun register() {
        bind = registerKeybinding(name, category, type, code)
    }

    enum class Type {
        MOUSE,
        KEYBOARD
    }
}
