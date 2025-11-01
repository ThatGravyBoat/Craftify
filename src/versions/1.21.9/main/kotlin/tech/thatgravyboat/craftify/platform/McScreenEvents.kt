package tech.thatgravyboat.craftify.platform

import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.input.CharacterEvent as McCharacterEvent
import net.minecraft.client.input.KeyEvent as McKeyEvent
import net.minecraft.client.input.MouseButtonEvent as McMouseButtonEvent
import net.minecraft.client.input.MouseButtonInfo as McMouseButtonInfo

class CharacterEventImpl(val mc: McCharacterEvent): CharacterEvent {
    override val codepoint: Int get() = mc.codepoint
    override val modifiers: Int get() = mc.modifiers
}

class KeyEventImpl(val mc: McKeyEvent): KeyEvent {
    override val key: Int get() = mc.key
    override val scancode: Int get() = mc.scancode
    override val modifiers: Int get() = mc.modifiers
}

open class MouseEventImpl(val mc: McMouseButtonEvent): MouseEvent {
    override val button: Int get() = mc.buttonInfo.button
    override val x: Double get() = mc.x
    override val y: Double get() = mc.y
    override val modifiers: Int get() = mc.buttonInfo.modifiers
}

class MouseDraggedEventImpl(mc: McMouseButtonEvent, override val deltaX: Double, override val deltaY: Double): MouseEventImpl(mc), MouseDraggedEvent

fun McCharacterEvent.toPlatform(): CharacterEvent = CharacterEventImpl(this)
fun McKeyEvent.toPlatform(): KeyEvent = KeyEventImpl(this)
fun McMouseButtonEvent.toPlatform(): MouseEvent = MouseEventImpl(this)
fun McMouseButtonEvent.toPlatform(deltaX: Double, deltaY: Double): MouseDraggedEvent = MouseDraggedEventImpl(this, deltaX, deltaY)

fun CharacterEvent.toMc(): McCharacterEvent = if (this is CharacterEventImpl) this.mc else McCharacterEvent(codepoint, modifiers)
fun KeyEvent.toMc(): McKeyEvent = if (this is KeyEventImpl) this.mc else McKeyEvent(key, scancode, modifiers)
fun MouseEvent.toMc(): McMouseButtonEvent = if (this is MouseEventImpl) this.mc else McMouseButtonEvent(x, y, McMouseButtonInfo(button, modifiers))

actual fun AbstractWidget.mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean =
    this.mouseClicked(McMouseButtonEvent(mouseX, mouseY, McMouseButtonInfo(button, 0)), false)