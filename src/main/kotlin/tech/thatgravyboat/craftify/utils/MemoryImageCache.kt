package tech.thatgravyboat.craftify.utils

import gg.essential.elementa.components.image.ImageCache
import java.awt.image.BufferedImage
import java.net.URL
import kotlin.collections.LinkedHashMap
import kotlin.collections.MutableMap
import kotlin.collections.set

class MemoryImageCache private constructor(private val maxCacheSize: Int = 100) : ImageCache {

    private var cache = object : LinkedHashMap<URL, BufferedImage>() {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<URL, BufferedImage>?): Boolean {
            return size > maxCacheSize
        }
    }

    override fun get(url: URL): BufferedImage? {
        return cache[url]
    }

    override fun set(url: URL, image: BufferedImage) {
        cache[url] = image
    }

    fun getOrSet(url: URL, provider: (URL) -> BufferedImage): BufferedImage {
        return cache.computeIfAbsent(url, provider)
    }

    companion object {
        val UI_ELEMENTS = MemoryImageCache(100)
        val COVER_IMAGE = MemoryImageCache(1)
    }
}
