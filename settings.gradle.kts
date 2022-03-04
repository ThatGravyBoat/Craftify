rootProject.name = "Craftify"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.minecraftforge.net")
        maven("https://jitpack.io/")
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "net.minecraftforge.gradle.forge" -> {
                    useModule("com.github.asbyth:ForgeGradle:${requested.version}")
                }
            }
        }
    }
}
