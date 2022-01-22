package tech.thatgravyboat.craftify.lib

import gg.essential.elementa.components.image.ImageCache
import java.awt.image.BufferedImage
import java.net.URL

object SingleImageCache : ImageCache {

    private var lastURL: URL? = null
    private var lastImage: BufferedImage? = null

    override fun get(url: URL): BufferedImage? {
        return if (url.path.equals(lastURL?.path)) lastImage else null
    }

    override fun set(url: URL, image: BufferedImage) {
        lastURL = url
        lastImage = image
    }
}
