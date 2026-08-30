import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    kotlin("jvm") version "2.3.0"
    alias(libs.plugins.loom)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
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

    "implementation"(libs.vigilance) {
        isTransitive = false
    }
    "include"(libs.vigilance)
    
    "implementation"(libs.elementa) {
        isTransitive = false
    }
    "include"(libs.elementa)
    
    "implementation"(libs.universalcraft) {
        exclude("org.jetbrains.kotlinx")
        exclude("org.jetbrains.kotlin")
        exclude("net.fabricmc")
    }
    "include"(libs.universalcraft)
    
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

tasks.withType<JavaCompile>().configureEach {
    options.release.set(25)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_25)
    compilerOptions {
        languageVersion = KotlinVersion.KOTLIN_2_0
    }
}