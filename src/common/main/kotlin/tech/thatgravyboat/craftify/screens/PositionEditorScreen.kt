package tech.thatgravyboat.craftify.screens

import net.minecraft.client.gui.screens.Screen

expect fun PositionEditorScreen(): Screen
expect fun Screen.isPositionEditor(): Boolean