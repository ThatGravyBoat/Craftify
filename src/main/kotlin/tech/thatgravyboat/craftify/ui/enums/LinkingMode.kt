package tech.thatgravyboat.craftify.ui.enums

import gg.essential.universal.UDesktop
import tech.thatgravyboat.craftify.utils.Utils
import java.net.URI

enum class LinkingMode {
    OPEN {
        override fun copy(url: URI) = Utils.browse(url.toString())
    },

    COPY {
        override fun copy(url: URI): Boolean {
            UDesktop.setClipboardString(url.toString())
            return true
        }
    },

    CHAT {
        override fun copy(url: URI) = false
    };

    open fun copy(url: URI): Boolean {
        throw NotImplementedError()
    }

    override fun toString(): String = when (this) {
        OPEN -> "Open in Browser"
        COPY -> "Copy to Clipboard"
        CHAT -> "Put in Chat"
    }
}
