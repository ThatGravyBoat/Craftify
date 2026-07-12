package tech.thatgravyboat.craftify.ui.enums

import gg.essential.universal.UDesktop
import tech.thatgravyboat.craftify.utils.Utils
import java.net.URI

enum class LinkingMode(val action: (uri: URI) -> Boolean) {
    OPEN({ Utils.browse(it.toString()) }),
    COPY({
        UDesktop.setClipboardString(it.toString())
        true
    }),
    CHAT( { false });

    fun copy(url: URI): Boolean = this.action(url)

    override fun toString(): String = when (this) {
        OPEN -> "Open in Browser"
        COPY -> "Copy to Clipboard"
        CHAT -> "Put in Chat"
    }
}
