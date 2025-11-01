package tech.thatgravyboat.craftify.ui.enums

enum class Alignment(
    val calculator: (Int, Int, Int) -> Int,
    val positioner: (Int, Int, Int) -> Int
) {
    TOP({ _, _, offset -> offset }, { _, _, mouse -> mouse }),
    LEFT({ _, _, offset -> offset }, { _, _, mouse -> mouse }),
    MIDDLE({ screen, component, offset -> (screen - component) / 2 + offset }, { screen, component, mouse -> mouse - (screen - component) / 2 }),
    RIGHT({ screen, component, offset -> screen - component + offset }, { screen, component, mouse -> mouse - (screen - component) }),
    BOTTOM({ screen, component, offset -> screen - component + offset }, { screen, component, mouse -> mouse - (screen - component) }),
}
