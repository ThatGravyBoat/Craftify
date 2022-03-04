plugins {
    java
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("net.minecraftforge.gradle.forge") version "ddb1eb0"
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
}

val modVersion: String by project

version = modVersion
group = "tech.thatgravyboat"
base {
    archivesBaseName = "Craftify"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

minecraft {
    version = "1.8.9-11.15.1.2318-1.8.9"
    runDir = "run"
    mappings = "stable_22"
    makeObfSourceJar = false

    clientRunArgs += listOf("--tweakClass gg.essential.loader.stage0.EssentialSetupTweaker")
}

val shadowed: Configuration by configurations.creating

configurations.compile {
    extendsFrom(shadowed)
}

repositories {
    mavenCentral()
    maven(url = "https://repo.sk1er.club/repository/maven-public")
}

dependencies {
    shadowed("gg.essential:loader-launchwrapper:1.1.3")
    implementation("gg.essential:essential-1.8.9-forge:1933")
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("mcversion", project.minecraft.version)
    filesMatching("mcmod.info") {
        expand(mapOf("version" to project.version, "mcversion" to project.minecraft.version))
    }
}

val moveResources by tasks.registering(Copy::class) {
    sourceSets.main {
        from(output.resourcesDir)
        java {
            destinationDir = classesDirectory.get().asFile
        }
    }
    dependsOn(tasks.processResources)
}

tasks.classes {
    dependsOn(moveResources)
}

tasks.reobfJar {
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    archiveClassifier.set("")
    configurations = listOf(shadowed)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=enable")
    }
}
