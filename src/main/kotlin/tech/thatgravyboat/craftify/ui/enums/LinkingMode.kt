package tech.thatgravyboat.craftify.ui.enums

import gg.essential.universal.UDesktop
import tech.thatgravyboat.craftify.utils.EssentialApiHelper
import java.net.URI

enum class LinkingMode {
    OPEN {
        override fun copy(url: URI) = EssentialApiHelper.browse(url.toString())
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
}
