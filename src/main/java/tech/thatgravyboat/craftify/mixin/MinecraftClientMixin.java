package tech.thatgravyboat.craftify.mixin;

//#if FABRIC==1
//$$ import net.minecraft.client.MinecraftClient;
//$$ import net.minecraft.client.gui.screen.Screen;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$
//$$ @Mixin(MinecraftClient.class)
//$$ public class MinecraftClientMixin {
//$$     @Inject(method = "setScreen", at = @At("HEAD"))
//$$     private void onSetScreen(Screen screen, CallbackInfo ci) {
//$$         tech.thatgravyboat.craftify.platform.EventsKt.getEventBus().post(new tech.thatgravyboat.craftify.platform.ScreenOpenEvent(screen));
//$$     }
//$$
//$$     @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/ToastManager;draw(Lnet/minecraft/client/util/math/MatrixStack;)V", shift = At.Shift.AFTER))
//$$     private void onRender(boolean tick, CallbackInfo ci) {
//$$         if (MinecraftClient.getInstance().world == null) return;
//$$         tech.thatgravyboat.craftify.platform.EventsKt.getEventBus().post(new tech.thatgravyboat.craftify.platform.RenderEvent(new gg.essential.universal.UMatrixStack()));
//$$     }
//$$ }
//$$
//#endif