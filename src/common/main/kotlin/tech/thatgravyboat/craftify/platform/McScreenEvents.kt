package tech.thatgravyboat.craftify.platform

import net.minecraft.client.gui.components.AbstractWidget

interface CharacterEvent {
    val codepoint: Int
    val modifiers: Int

    val character: Char get() = codepoint.toChar()
}

interface KeyEvent {
    val key: Int
    val scancode: Int
    val modifiers: Int
}

interface MouseEvent {
    val button: Int
    val x: Double
    val y: Double
    val modifiers: Int
}

interface MouseDraggedEvent: MouseEvent {
    val deltaX: Double
    val deltaY: Double
}

expect fun AbstractWidget.mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean