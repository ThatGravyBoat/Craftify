package tech.thatgravyboat.craftify.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tech.thatgravyboat.craftify.ui.Player;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @WrapOperation(method = "onButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseClicked(Lnet/minecraft/client/input/MouseButtonEvent;Z)Z"))
    private boolean onSetScreen(Screen instance, MouseButtonEvent event, boolean doubleClicked, Operation<Boolean> original) {
        if (Player.INSTANCE.onMouseClicked(event.x(), event.y(), event.button())) {
            return true;
        }
        return original.call(instance, event, doubleClicked);
    }
}