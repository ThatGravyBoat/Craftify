package tech.thatgravyboat.craftify.platform

import net.minecraft.client.gui.components.AbstractWidget

actual fun AbstractWidget.mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean = this.mouseClicked(mouseX, mouseY, button)