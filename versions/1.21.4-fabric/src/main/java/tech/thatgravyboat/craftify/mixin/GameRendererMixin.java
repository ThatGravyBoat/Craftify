package tech.thatgravyboat.craftify.mixin;

import gg.essential.universal.UMatrixStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.thatgravyboat.craftify.platform.compat.obsoverlay.ObsOverlayCompat;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE:LAST",
            target = "Lnet/minecraft/client/gui/DrawContext;draw()V",
            shift = At.Shift.BEFORE
        )
    )
    private void onRenderNormal(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        if (MinecraftClient.getInstance().world == null) return;
        if (ObsOverlayCompat.INSTANCE.getEnabled()) return;
        tech.thatgravyboat.craftify.platform.Events.INSTANCE.getRENDER().post(new UMatrixStack());
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;getOverlay()Lnet/minecraft/client/gui/screen/Overlay;",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            )
    )
    private void onRenderObs(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        if (MinecraftClient.getInstance().world == null) return;
        if (!ObsOverlayCompat.INSTANCE.getEnabled()) return;
        tech.thatgravyboat.craftify.platform.Events.INSTANCE.getRENDER().post(new UMatrixStack());
    }
}
