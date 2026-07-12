package tech.thatgravyboat.craftify.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.thatgravyboat.craftify.screens.PositionEditorScreen;
import tech.thatgravyboat.craftify.ui.Player;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE:LAST",
                    target = "Lnet/minecraft/client/gui/Gui;renderDebugOverlay(Lnet/minecraft/client/gui/GuiGraphics;)V"
            )
    )
    private void onRenderNormal(DeltaTracker tracker, boolean tick, CallbackInfo ci, @Local(ordinal = 0) GuiGraphics graphics) {
        var mc = Minecraft.getInstance();
        var window = mc.getWindow();
        var hasScreen = mc.screen != null && !(mc.screen instanceof PositionEditorScreen);
        if (mc.level == null) return;

        graphics.pose().pushMatrix();
        Player.INSTANCE.onRender(
                graphics,
                hasScreen ? (int) mc.mouseHandler.getScaledXPos(window) : -1,
                hasScreen ? (int) mc.mouseHandler.getScaledYPos(window) : -1,
                tracker.getGameTimeDeltaPartialTick(false)
        );
        graphics.pose().popMatrix();
    }
}
