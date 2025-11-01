package tech.thatgravyboat.craftify.platform

import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.resources.ResourceLocation

private val CATEGORIES: MutableMap<ResourceLocation, KeyMapping.Category> = mutableMapOf()

private class McKeybindImpl(
    private val name: String,
    private val category: ResourceLocation,
    private val type: InputConstants.Type,
    private val code: Int,
) : McKeybind {
    override lateinit var mapping: KeyMapping

    override fun register() {
        mapping = KeyBindingHelper.registerKeyBinding(
            KeyMapping(name, type, code, CATEGORIES.computeIfAbsent(this.category, KeyMapping.Category::register))
        )
    }
}

actual fun McKeybind(name: String, category: ResourceLocation, type: InputConstants.Type, code: Int): McKeybind {
    return McKeybindImpl(name, category, type, code)
}