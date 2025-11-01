package tech.thatgravyboat.craftify.platform

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import net.minecraft.resources.ResourceLocation

interface McKeybind {
    val mapping: KeyMapping

    val isPressed: Boolean get() = mapping.isDown && !mapping.isUnbound

    fun register()
}

expect fun McKeybind(name: String, category: ResourceLocation, type: InputConstants.Type, code: Int): McKeybind