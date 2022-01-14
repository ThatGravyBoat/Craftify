package tech.thatgravyboat.craftify.ui.enums.copying

import gg.essential.universal.UDesktop
import java.net.URI

enum class LinkingMode : CopyingType {
    OPEN {
        override fun copy(url: URI) = UDesktop.browse(url)
    },

    COPY {
        override fun copy(url: URI): Boolean {
            UDesktop.setClipboardString(url.toString())
            return true
        }
    }
}
