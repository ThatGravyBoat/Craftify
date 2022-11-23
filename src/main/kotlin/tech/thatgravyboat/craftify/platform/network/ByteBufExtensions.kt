package tech.thatgravyboat.craftify.platform.network

import com.google.common.base.Charsets
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.EncoderException

fun ByteBuf.writeString(string: String) {
    val bs = string.toByteArray(Charsets.UTF_8)
    if (bs.size > 32767) {
        throw EncoderException("String too big (was " + string.length + " bytes encoded, max " + 32767 + ")")
    } else {
        this.writeVarInt(bs.size)
        this.writeBytes(bs)
    }
}

fun ByteBuf.writeVarInt(i: Int) {
    var input = i
    while (input and -128 != 0) {
        writeByte(input and 127 or 128)
        input = input ushr 7
    }
    writeByte(input)
}

fun <T> ByteBuf.writeCollection(collection: Collection<T>, writer: (ByteBuf, T) -> Unit) {
    writeVarInt(collection.size)
    collection.forEach { writer(this, it) }
}