package tech.thatgravyboat.craftify.mixin;
//#if FABRIC==1
//$$ import gg.essential.lib.mixinextras.injector.WrapWithCondition;
//$$ import net.minecraft.client.Mouse;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$
//$$ @Mixin(Mouse.class)
//$$ public class MouseMixin {
//$$     @WrapWithCondition(method = "onMouseButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;wrapScreenError(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V", ordinal = 0))
//$$     private boolean shouldHandleButton(Runnable task, String errorTitle, String screenName, long window, int button, int action, int mods) {
//$$         tech.thatgravyboat.craftify.platform.MouseClickEvent event = new tech.thatgravyboat.craftify.platform.MouseClickEvent(button);
//$$         tech.thatgravyboat.craftify.platform.EventsKt.getEventBus().post(event);
//$$         return !event.getCancelled();
//$$     }
//$$ }
//$$
//#endif