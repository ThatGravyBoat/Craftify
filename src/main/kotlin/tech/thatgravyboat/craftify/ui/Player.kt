package tech.thatgravyboat.craftify.ui

import gg.essential.api.utils.GuiUtil
import gg.essential.elementa.components.Window
import gg.essential.elementa.dsl.childOf
import gg.essential.universal.UMouse
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Mouse
import tech.thatgravyboat.craftify.Config
import tech.thatgravyboat.craftify.types.PlayerState
import tech.thatgravyboat.craftify.ui.enums.Position
import tech.thatgravyboat.craftify.ui.enums.RenderType

object Player {

    private val window = Window()
    private val player = UiPlayer() childOf window

    init {
        changePosition(Position.values()[Config.position])
    }

    fun stopClient() {
        player.clientStop()
    }

    fun updatePlayer(state: PlayerState) {
        player.updateState(state)
    }

    fun changePosition(position: Position) {
        player.setX(position.x(player))
        player.setY(position.y(player))
    }

    @SubscribeEvent
    fun onRender(event: TickEvent.RenderTickEvent) {
        if (event.phase.equals(TickEvent.Phase.START)) return
        val renderType = RenderType.values()[Config.renderType]
        if (renderType.canRender(GuiUtil.getOpenedScreen()) && Config.enable) {
            window.draw()
        }
    }

    // XY values taken from GuiScreen go there if anything screws up.
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onMouseClicked(event: GuiScreenEvent.MouseInputEvent.Pre) {
        if (!Config.enable) return
        val renderType = RenderType.values()[Config.renderType]
        if (renderType.canRender(GuiUtil.getOpenedScreen()) && Config.enable && player.isHovered() && Mouse.getEventButtonState()) {
            val button = Mouse.getEventButton()
            player.mouseClick(UMouse.Scaled.x, UMouse.Scaled.y, button)
            event.isCanceled = true
        }
    }
}
