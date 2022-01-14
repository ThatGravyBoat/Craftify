package tech.thatgravyboat.craftify.ui.enums.copying

import java.net.URI

interface CopyingType {
    fun copy(url: URI): Boolean
}
