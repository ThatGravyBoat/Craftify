package tech.thatgravyboat.craftify.platform.network

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.minecraft.client.MinecraftClient
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket
import net.minecraft.util.Identifier

object PacketHandler {
    fun sendPacket(id: String, data: (ByteBuf) -> Unit) {
        val buf = Unpooled.buffer()
        data(buf)
        MinecraftClient.getInstance().networkHandler?.sendPacket(CustomPayloadC2SPacket(Identifier(id), PacketByteBuf(buf)))
    }

    fun getServerAddress(): String {
        return MinecraftClient.getInstance().currentServerEntry?.address ?: ""
    }

    fun isOnLanServer(): Boolean {
        return MinecraftClient.getInstance().currentServerEntry?.isLocal ?: false
    }
}