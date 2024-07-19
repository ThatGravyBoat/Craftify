pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://repo.polyfrost.org/releases")
    }
    plugins {
        val egtVersion = "0.6.1"
        id("gg.essential.multi-version.root") version egtVersion
    }
}

val mod_name: String by settings

rootProject.name = mod_name
rootProject.buildFileName = "root.gradle.kts"

listOf(
    "1.8.9-forge",
    "1.12.2-forge",
    "1.17.1-forge",
    "1.17.1-fabric",
    "1.18.1-fabric",
    "1.19-fabric",
    "1.19.3-fabric",
    "1.19.4-fabric",
    "1.20.1-fabric",
    "1.20.2-fabric",
    "1.20.4-fabric",
    "1.20.6-fabric",
    "1.21-fabric",
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }

}