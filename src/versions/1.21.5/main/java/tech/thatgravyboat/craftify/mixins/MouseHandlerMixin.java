package tech.thatgravyboat.craftify.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tech.thatgravyboat.craftify.ui.Player;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @WrapOperation(method = "onPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseClicked(DDI)Z"))
    private boolean onSetScreen(Screen instance, double mouseX, double mouseY, int button, Operation<Boolean> original) {
        if (Player.INSTANCE.onMouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return original.call(instance, mouseX, mouseY, button);
    }
}