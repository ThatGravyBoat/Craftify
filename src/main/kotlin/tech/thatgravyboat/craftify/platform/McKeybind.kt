package tech.thatgravyboat.craftify.platform

import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.resources.Identifier

private val CATEGORIES: MutableMap<Identifier, KeyMapping.Category> = mutableMapOf()

class McKeybind(
    private val name: String,
    private val category: Identifier,
    private val type: InputConstants.Type,
    private val code: Int,
) {
    lateinit var mapping: KeyMapping

    val isPressed: Boolean get() = mapping.isDown && !mapping.isUnbound

    fun register() {
        mapping = KeyBindingHelper.registerKeyBinding(
            KeyMapping(name, type, code, CATEGORIES.computeIfAbsent(this.category, KeyMapping.Category::register))
        )
    }
}