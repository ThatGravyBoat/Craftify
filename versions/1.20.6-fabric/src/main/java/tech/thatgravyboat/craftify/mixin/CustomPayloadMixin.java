package tech.thatgravyboat.craftify.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import tech.thatgravyboat.craftify.platform.network.GenericCustomPayload;

@Mixin(targets = "net/minecraft/network/packet/CustomPayload$1")
public class CustomPayloadMixin<B extends PacketByteBuf> {

    @WrapOperation(
            method = "encode(Lnet/minecraft/network/PacketByteBuf;Lnet/minecraft/network/packet/CustomPayload;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/packet/CustomPayload$1;encode(Lnet/minecraft/network/PacketByteBuf;Lnet/minecraft/network/packet/CustomPayload$Id;Lnet/minecraft/network/packet/CustomPayload;)V"
            )
    )
    private void encode(@Coerce PacketCodec<B, CustomPayload> instance, PacketByteBuf buf, CustomPayload.Id<?> id, CustomPayload payload, Operation<Void> operation) {
        if (payload instanceof GenericCustomPayload genericPayload) {
            buf.writeIdentifier(Identifier.tryParse(genericPayload.getChannel()));
            genericPayload.write(buf);
        } else {
            operation.call(instance, buf, id, payload);
        }

    }
}
