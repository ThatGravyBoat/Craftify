package tech.thatgravyboat.craftify.platform

import com.mojang.blaze3d.platform.Window
import net.minecraft.client.Minecraft
import net.minecraft.client.Options
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.scores.DisplaySlot

object McClient {

    val self: Minecraft get() = Minecraft.getInstance()
    val font: Font get() = self.font
    var screen: Screen?
        get() = self.gui.screen()
        set(value) {
            if (self.gui.screen() != null) {
                run { self.gui.setScreen(value) }
            } else {
                self.gui.setScreen(value)
            }
        }
    val player: LocalPlayer? get() = self.player
    val options: Options get() = self.options
    val window: Window get() = self.window

    val hideGui: Boolean get() = self.gui.hud.isHidden
    val showDebugGui: Boolean get() = self.debugEntries.isOverlayVisible
    val isTabOpened: Boolean get() {
        if (!options.keyPlayerList.isDown) return false

        val players = self.connection?.listedOnlinePlayers?.size ?: 0
        val scoreboard = self.level?.scoreboard?.getDisplayObjective(DisplaySlot.LIST)

        return self.isLocalServer || players > 1 || scoreboard != null
    }

    // server stuff

    val serverIp: String? get() = self.currentServer?.ip
    val isLanServer: Boolean get() = self.currentServer?.type() == ServerData.Type.LAN

    fun run(nextTick: Boolean = true, action: () -> Unit) {
        if (nextTick) {
            self.schedule(action)
        } else {
            self.execute(action)
        }
    }
}