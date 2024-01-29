import gg.essential.gradle.util.*

plugins {
    kotlin("jvm")
    id("gg.essential.multi-version")
    id("gg.essential.defaults")
    id("com.github.johnrengelman.shadow")
    id("net.kyori.blossom")
}

val mod_name: String by project
val mod_version: String by project
val mod_id: String by project

preprocess {
    vars.put("MODERN", if (project.platform.mcMinor >= 16) 1 else 0)
}

blossom {
    replaceToken("@VER@", mod_version)
    replaceToken("@NAME@", mod_name)
    replaceToken("@ID@", mod_id)
    replaceToken("@DEBUG@", true)
}

version = mod_version
group = "tech.thatgravyboat"
base {
    archivesName.set(mod_name)
}

tasks.compileKotlin.setJvmDefault(if (platform.mcVersion >= 11400) "all" else "all-compatibility")
loom.noServerRunConfigs()
loom {
//    if (project.platform.isLegacyForge) {
//        launchConfigs.named("client") {
//            arg("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
//        }
//    }
    mixin.defaultRefmapName.set("mixins.${mod_id}.refmap.json")
}

repositories {
    maven("https://maven.resourcefulbees.com/repository/thatgravyboat/")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://repo.essential.gg/repository/maven-public/")
}

val shade: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    val elementa_version: String by project
    val vigilance_version: String by project
    val universal_version: String by project
    val essential_version: String? by project
    if (platform.isFabric) {
        val fabricApiVersion: String by project
        val fabricLanguageKotlinVersion: String by project
        val modMenuVersion: String by project
        modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
        modImplementation("net.fabricmc:fabric-language-kotlin:$fabricLanguageKotlinVersion")
        modImplementation("com.terraformersmc:modmenu:$modMenuVersion")
        modImplementation("include"("gg.essential:elementa-${elementa_version}")!!)
        modImplementation("include"("gg.essential:vigilance-${vigilance_version}")!!)
        modImplementation("include"("gg.essential:universalcraft-${universal_version}")!!)
//        runtimeOnly("gg.essential:loader-fabric:1.0.0")
    } else {
        compileOnly("gg.essential:essential-${essential_version ?: platform}")
        shade("gg.essential:loader-launchwrapper:1.1.3") {
            isTransitive = false
        }
    }
    shade("io.ktor:ktor-client-core-jvm:2.1.0") {
        exclude("org.jetbrains.kotlinx")
        exclude("org.jetbrains.kotlin")
        exclude("org.slf4j")
    }
    shade("io.ktor:ktor-client-cio-jvm:2.1.0") {
        exclude("org.jetbrains.kotlinx")
        exclude("org.jetbrains.kotlin")
        exclude("org.slf4j")
    }
    shade("tech.thatgravyboat:jukebox-jvm:1.0-20231031.095916-26") {
        isTransitive = false
    }
}

tasks.processResources {
    inputs.property("id", mod_id)
    inputs.property("name", mod_name)
    val java = if (project.platform.mcMinor >= 18) {
        17
    } else {if (project.platform.mcMinor == 17) 16 else 8 }
    val compatLevel = "JAVA_${java}"
    inputs.property("java", java)
    inputs.property("java_level", compatLevel)
    inputs.property("version", mod_version)
    inputs.property("mcVersionStr", project.platform.mcVersionStr)
    filesMatching(listOf("mcmod.info", "mixins.${mod_id}.json", "mods.toml")) {
        expand(mapOf(
            "id" to mod_id,
            "name" to mod_name,
            "java" to java,
            "java_level" to compatLevel,
            "version" to mod_version,
            "mcVersionStr" to project.platform.mcVersionStr
        ))
    }
    filesMatching("fabric.mod.json") {
        expand(mapOf(
            "id" to mod_id,
            "name" to mod_name,
            "java" to java,
            "java_level" to compatLevel,
            "version" to mod_version,
            "mcVersionStr" to project.platform.mcVersionStr.substringBeforeLast(".") + ".x"
        ))
    }
}

tasks {
    withType(Jar::class.java) {
        if (project.platform.isFabric) {
            exclude("mcmod.info", "mods.toml")
        } else {
            exclude("fabric.mod.json", "mixins.${mod_id}.json")
            if (project.platform.isLegacyForge) {
                exclude("mods.toml")
            } else {
                exclude("mcmod.info")
            }
        }
    }
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveClassifier.set("dev")
        configurations = listOf(shade)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    remapJar {
        input.set(shadowJar.get().archiveFile)
        archiveClassifier.set("")
    }
    jar {
        if (project.platform.isLegacyForge) {
            manifest {
                attributes(
                    mapOf(
                        "ModSide" to "CLIENT",
                        "TweakOrder" to "0",
                        "TweakClass" to "gg.essential.loader.stage0.EssentialSetupTweaker",
                        "ForceLoadAsMod" to true
                    )
                )
            }
        }
        dependsOn(shadowJar)
        archiveClassifier.set("")
        enabled = false
    }
}
