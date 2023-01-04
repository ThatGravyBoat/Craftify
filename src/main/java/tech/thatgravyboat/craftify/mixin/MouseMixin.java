package tech.thatgravyboat.craftify.mixin;
//#if FABRIC==1
//$$ import net.minecraft.client.Mouse;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$
//$$ import net.minecraft.client.gui.screen.Screen;
//$$
//$$ @Mixin(Mouse.class)
//$$ public class MouseMixin {
//$$     @Inject(method = "method_1611([ZLnet/minecraft/client/gui/screen/Screen;DDI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;mouseClicked(DDI)Z"), cancellable = true)
//$$     private static void craftify$onMouseClicked(boolean[] resultHack, Screen screen, double mouseX, double mouseY, int button, CallbackInfo ci) {
//$$         if (tech.thatgravyboat.craftify.platform.Events.INSTANCE.getMOUSE_CLICKED().post(button)) {
//$$             ci.cancel();
//$$         }
//$$     }
//$$ }
//$$
//#endif