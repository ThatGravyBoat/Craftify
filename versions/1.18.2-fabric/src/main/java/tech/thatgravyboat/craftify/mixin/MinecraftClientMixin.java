package tech.thatgravyboat.craftify.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "setScreen", at = @At("HEAD"))
    private void onSetScreen(Screen screen, CallbackInfo ci) {
        tech.thatgravyboat.craftify.platform.Events.INSTANCE.getSCREEN_CHANGED().post(screen);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/ToastManager;draw(Lnet/minecraft/client/util/math/MatrixStack;)V", shift = At.Shift.AFTER))
    private void onRender(boolean tick, CallbackInfo ci) {
        if (MinecraftClient.getInstance().world == null) return;
        tech.thatgravyboat.craftify.platform.Events.INSTANCE.getRENDER().post(new gg.essential.universal.UMatrixStack());
    }
}

