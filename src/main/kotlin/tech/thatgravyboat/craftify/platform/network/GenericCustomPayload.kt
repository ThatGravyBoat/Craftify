package tech.thatgravyboat.craftify.platform.network

import io.netty.buffer.ByteBuf

interface GenericCustomPayload {

    val channel: String

    fun write(buffer: ByteBuf)
}