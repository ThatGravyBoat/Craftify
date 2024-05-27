package tech.thatgravyboat.craftify.utils

import gg.essential.elementa.components.image.ImageCache
import java.awt.image.BufferedImage
import java.net.URL

object MemoryImageCache : ImageCache {

    private const val MAX_CACHE_SIZE = 100

    private var cache = object : LinkedHashMap<URL, BufferedImage>() {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<URL, BufferedImage>?): Boolean {
            return size > MAX_CACHE_SIZE
        }
    }

    override fun get(url: URL): BufferedImage? {
        return cache[url]
    }

    override fun set(url: URL, image: BufferedImage) {
        cache[url] = image
    }
}
