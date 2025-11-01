package tech.thatgravyboat.craftify.platform

import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents

abstract class McScreen : Screen {

    constructor(): super(CommonComponents.EMPTY)

    open fun mouseClicked(event: MouseEvent): Boolean = false
    open fun mouseReleased(event: MouseEvent): Boolean = false
    open fun mouseDragged(event: MouseDraggedEvent): Boolean = false
    open fun keyPressed(event: KeyEvent): Boolean = false
    open fun keyReleased(event: KeyEvent): Boolean = false
    open fun charTyped(event: CharacterEvent): Boolean = false
}