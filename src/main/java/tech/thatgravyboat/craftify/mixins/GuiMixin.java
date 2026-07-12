package tech.thatgravyboat.craftify.mixins;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.thatgravyboat.craftify.screens.PositionEditorScreen;
import tech.thatgravyboat.craftify.ui.Player;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(
            method = "extractRenderState",
            at = @At("TAIL")
    )
    private void onRenderNormal(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        var mc = Minecraft.getInstance();

        if (mc.screen instanceof PositionEditorScreen) return;

        var window = mc.getWindow();
        var hasScreen = mc.screen != null;
        if (mc.level == null) return;

        graphics.pose().pushMatrix();
        Player.INSTANCE.onRender(
                graphics,
                hasScreen ? (int) mc.mouseHandler.getScaledXPos(window) : -1,
                hasScreen ? (int) mc.mouseHandler.getScaledYPos(window) : -1,
                deltaTracker.getGameTimeDeltaPartialTick(false)
        );
        graphics.pose().popMatrix();
    }
}
