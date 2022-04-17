plugins {
    kotlin("jvm") version "1.6.10" apply false
    id("net.kyori.blossom") version "1.3.0" apply false
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
    id("gg.essential.multi-version.root")
}

preprocess {
    "1.18.1-fabric"(11801, "yarn") {
        "1.17.1-fabric"(11701, "yarn") {
            "1.17.1-forge"(11701, "srg") {
                "1.12.2-forge"(11202, "srg") {
                    "1.8.9-forge"(10809, "srg", file("versions/1.12.2-1.8.9.txt"))
                }
            }
        }
    }
}