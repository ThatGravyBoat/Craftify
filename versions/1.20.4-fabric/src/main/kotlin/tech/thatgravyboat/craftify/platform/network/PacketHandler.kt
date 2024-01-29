package tech.thatgravyboat.craftify.platform.network

import io.netty.buffer.ByteBuf
import net.minecraft.client.MinecraftClient
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket
import net.minecraft.util.Identifier

object PacketHandler {
    fun sendPacket(id: String, data: (ByteBuf) -> Unit) {
        MinecraftClient.getInstance().networkHandler?.sendPacket(CustomPayloadC2SPacket(GenericPacketPayload(id, data)))
    }

    fun getServerAddress(): String {
        return MinecraftClient.getInstance().currentServerEntry?.address ?: ""
    }

    fun isOnLanServer(): Boolean {
        return MinecraftClient.getInstance().currentServerEntry?.isLocal ?: false
    }
}

class GenericPacketPayload(val id: String, val data: (ByteBuf) -> Unit) : CustomPayload {
    override fun write(buf: PacketByteBuf?) {
        if (buf != null) {
            data(buf)
        }
    }

    override fun id(): Identifier {
        return Identifier(id)
    }

}