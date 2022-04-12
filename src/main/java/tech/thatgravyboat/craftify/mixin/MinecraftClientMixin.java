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
//$$         tech.thatgravyboat.craftify.EventsKt.getEventBus().post(new tech.thatgravyboat.craftify.ScreenOpenEvent(screen));
//$$     }
//$$
//$$     @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;preloadShaders(Lnet/minecraft/resource/ResourceFactory;)V", shift = At.Shift.AFTER))
//$$     private void initializeCraftify(@org.spongepowered.asm.mixin.injection.Coerce Object a, CallbackInfo ci) {
//$$        tech.thatgravyboat.craftify.Craftify.INSTANCE.onInit();
//$$     }
//$$ }
//$$
//#endif