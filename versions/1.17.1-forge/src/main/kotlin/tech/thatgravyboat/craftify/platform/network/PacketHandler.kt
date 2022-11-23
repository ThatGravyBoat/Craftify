package tech.thatgravyboat.craftify.platform.network

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket
import net.minecraft.resources.ResourceLocation

object PacketHandler {
    fun sendPacket(id: String, data: (ByteBuf) -> Unit) {
        val buf = Unpooled.buffer()
        data(buf)
        Minecraft.getInstance().connection?.send(ServerboundCustomPayloadPacket(ResourceLocation(id), FriendlyByteBuf(buf)))
    }

    fun getServerAddress(): String {
        return Minecraft.getInstance().currentServer?.ip ?: ""
    }

    fun isOnLanServer(): Boolean {
        return Minecraft.getInstance().currentServer?.isLan ?: false
    }
}