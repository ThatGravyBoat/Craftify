import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    kotlin("jvm") version "2.1.0"
    alias(libs.plugins.terrarium.cloche)
    alias(libs.plugins.classextensions)
}

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://repo.essential.gg/repository/maven-public")
    maven(url = "https://maven.teamresourceful.com/repository/maven-public")
    maven(url = "https://maven.msrandom.net/repository/root")
    maven(url = "https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

cloche {
    metadata {
        modId = "craftify"
        name = "Craftify"
        version = project.version.toString()
        license = "ARR"
        description = ""
        author("ThatGravyBoat")
    }

    common {
        mixins.from("src/mixins/craftify.mixins.json")

        dependencies {
            implementation(libs.vigilance) { isTransitive = false }
            implementation(libs.elementa) { isTransitive = false }

            implementation(libs.jukebox) { isTransitive = false }
            implementation(libs.ktor.cio) {
                exclude("org.jetbrains.kotlinx")
                exclude("org.jetbrains.kotlin")
            }
            implementation(libs.ktor.core) {
                exclude("org.jetbrains.kotlinx")
                exclude("org.jetbrains.kotlin")
                exclude("org.slf4j")
            }

            modImplementation(libs.fabric.kotlin)

            runtimeOnly(libs.devauth)
        }
    }

    fun createVersion(
        version: String,
        fabricVersion: String,
        olympus: Provider<MinimalExternalModuleDependency>,
        rlib: Provider<MinimalExternalModuleDependency>,
    ) {

        fabric("versions:$version") {
            includedClient()
            minecraftVersion = version
            loaderVersion = libs.versions.fabric.loader.get()

            metadata {
                entrypoint("client") {
                    adapter = "kotlin"
                    value = "tech.thatgravyboat.craftify.Craftify"
                }
            }

            dependencies {
                fabricApi(fabricVersion, minecraftVersion)
                modImplementation("gg.essential:universalcraft-${version}-fabric:${libs.versions.universalcraft.get()}")
                modImplementation(olympus)
                modImplementation(rlib)

                include(libs.vigilance)
                include(libs.elementa)
                include("gg.essential:universalcraft-${version}-fabric:${libs.versions.universalcraft.get()}")
                include(libs.jukebox)
                libs.bundles.ktor.get().forEach {
                    include(provider { it })
                }
                include(olympus) { isTransitive = false }
                include(rlib)
            }

            runs {
                client {
                    jvmArgs("-Ddevauth.enabled=true")
                }
            }
        }
    }

    createVersion("1.21.5", "0.128.1", libs.olympus.lib1215, libs.resourceful.lib1215)
    createVersion("1.21.9", "0.134.0", libs.olympus.lib1219, libs.resourceful.lib1219)
}


tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    compilerOptions {
        languageVersion = KotlinVersion.KOTLIN_2_0
        freeCompilerArgs.addAll(
            "-Xmulti-platform",
            "-Xno-check-actual",
            "-Xexpect-actual-classes",
        )
    }
}