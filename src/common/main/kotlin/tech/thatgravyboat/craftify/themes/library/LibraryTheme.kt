package tech.thatgravyboat.craftify.themes.library

import tech.thatgravyboat.craftify.themes.Theme
import java.net.URL

data class LibraryTheme(
    val id: String,
    val displayName: String,
    val author: String,
    val icon: URL,
    val screenshot: URL, // Change to array and add image carousel when more images may be needed.
    val description: String,
    val theme: Theme
)
