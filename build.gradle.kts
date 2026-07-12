import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    kotlin("jvm") version "2.3.0"
    alias(libs.plugins.loom)
    alias(libs.plugins.shadow)
}

loom {
    runs {
        named("client") {
            client()
            configName = "Minecraft Client - Fabric"
            ideConfigGenerated(true)
            runDir("run")
            vmArgs("-Ddevauth.enabled=true")
        }
    }
}

val shadowImplementation by configurations.creating {
    configurations["implementation"].extendsFrom(this)
}

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://maven.teamresourceful.com/repository/maven-public")
    maven(url = "https://repo.essential.gg/repository/maven-public")
    maven(url = "https://maven.msrandom.net/repository/root")
    maven(url = "https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

dependencies {
    "minecraft"(libs.minecraft)

    "implementation"(libs.fabric.loader)
    "implementation"(libs.fabric.api)
    "implementation"(libs.fabric.kotlin)

    "runtimeOnly"(libs.devauth)

    "shadowImplementation"(libs.vigilance) {
        isTransitive = false
    }
    "shadowImplementation"(libs.elementa) {
        isTransitive = false
    }
    "shadowImplementation"(libs.universalcraft) {
        exclude("org.jetbrains.kotlinx")
        exclude("org.jetbrains.kotlin")
        exclude("net.fabricmc")
    }
    "implementation"(libs.resourceful.lib) {
        "include"(this)
    }
    "implementation"(libs.olympus) {
        "include"(this)
    }

    implementation(libs.jukebox) {
        "include"(this)
        isTransitive = false
    }
    implementation(libs.ktor.cio) {
        exclude("org.jetbrains.kotlinx")
        exclude("org.jetbrains.kotlin")
    }
    implementation(libs.ktor.core) {
        exclude("org.jetbrains.kotlinx")
        exclude("org.jetbrains.kotlin")
        exclude("org.slf4j")
    }
    libs.bundles.ktor.get().forEach {
        "include"(provider { it })
    }
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("dev")
    configurations = listOf(shadowImplementation)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    relocate("gg.essential", "tech.thatgravyboat.craftify.libs.essential")
    exclude("pack.mcmeta")
    exclude("META-INF/maven/**")
}

tasks.withType<RemapJarTask> {
    inputFile.set(tasks.shadowJar.flatMap { it.archiveFile })
    archiveClassifier.set("")
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(25)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_25)
    compilerOptions {
        languageVersion = KotlinVersion.KOTLIN_2_0
    }
}