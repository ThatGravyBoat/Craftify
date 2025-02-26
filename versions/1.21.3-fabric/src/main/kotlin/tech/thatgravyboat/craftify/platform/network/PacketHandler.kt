package tech.thatgravyboat.craftify.platform.network

import io.netty.buffer.ByteBuf
import net.minecraft.client.MinecraftClient
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket

object PacketHandler {
    fun sendPacket(id: String, data: (ByteBuf) -> Unit) {
        MinecraftClient.getInstance().networkHandler?.sendPacket(CustomPayloadC2SPacket(Packet(id, data)))
    }

    fun getServerAddress(): String {
        return MinecraftClient.getInstance().currentServerEntry?.address ?: ""
    }

    fun isOnLanServer(): Boolean {
        return MinecraftClient.getInstance().currentServerEntry?.isLocal ?: false
    }

    private data class Packet(override val channel: String, val data: (ByteBuf) -> Unit) : CustomPayload, GenericCustomPayload {
        override fun getId(): CustomPayload.Id<out CustomPayload>? = null
        override fun write(buffer: ByteBuf) = data(buffer)
    }
}