package tech.thatgravyboat.craftify.platform

class UKeybind(private val name: String, private val category: String, private val type: Type, private val code: Int) {

    private lateinit var bind: MCKeyBinding

    fun getBinding() = bind

    fun register() {
        //#if MODERN==0 || FABRIC==1
        bind = registerKeybinding(name, category, type, code)
        //#endif
    }

    enum class Type {
        MOUSE,
        KEYBOARD
    }
}
