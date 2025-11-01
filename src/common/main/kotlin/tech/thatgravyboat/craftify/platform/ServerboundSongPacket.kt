package tech.thatgravyboat.craftify.platform

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import tech.thatgravyboat.craftify.Craftify
import java.util.*

class ServerboundSongPacket(
    title: String,
    artists: List<String>,
    link: Optional<String>,
) : CustomPacketPayload {

    // Enforce limits
    val title: String = title.take(256)
    val artists: List<String> = artists.take(2).map { it.take(256) }
    val link: Optional<String> = link.map { it.take(128) }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> = TYPE

    override fun toString(): String = "ServerboundSongPacket(title='$title', artists=$artists, link=$link)"

    companion object {

        val CODEC: StreamCodec<ByteBuf, ServerboundSongPacket> = StreamCodec.composite(
            ByteBufCodecs.stringUtf8(256), ServerboundSongPacket::title,
            ByteBufCodecs.stringUtf8(256).apply(ByteBufCodecs.list(2)), ServerboundSongPacket::artists,
            ByteBufCodecs.optional(ByteBufCodecs.stringUtf8(128)), ServerboundSongPacket::link,
            ::ServerboundSongPacket
        )

        val TYPE: CustomPacketPayload.Type<ServerboundSongPacket> = CustomPacketPayload.Type(Craftify.id("song"))
    }
}