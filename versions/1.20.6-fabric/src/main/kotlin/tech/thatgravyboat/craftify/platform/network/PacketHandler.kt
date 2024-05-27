package tech.thatgravyboat.craftify.platform.network

import io.netty.buffer.ByteBuf
import net.minecraft.client.MinecraftClient

object PacketHandler {
    fun sendPacket(id: String, data: (ByteBuf) -> Unit) {

    }

    fun getServerAddress(): String {
        return MinecraftClient.getInstance().currentServerEntry?.address ?: ""
    }

    fun isOnLanServer(): Boolean {
        return MinecraftClient.getInstance().currentServerEntry?.isLocal ?: false
    }
}