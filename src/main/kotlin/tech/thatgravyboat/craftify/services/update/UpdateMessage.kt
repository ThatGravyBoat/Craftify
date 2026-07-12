package tech.thatgravyboat.craftify.services.update

data class UpdateMessage(
    val version: UpdateVersion,
    val message: String
)
