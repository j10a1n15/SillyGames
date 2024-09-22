plugins {
    java
    kotlin("jvm") version ("2.0.0")
    val dgtVersion = "1.28.1"
    id("dev.deftu.gradle.tools") version (dgtVersion)
    id("dev.deftu.gradle.tools.shadow") version (dgtVersion)
    id("dev.deftu.gradle.tools.kotlin") version (dgtVersion)
    id("dev.deftu.gradle.tools.bloom") version (dgtVersion)
    id("dev.deftu.gradle.tools.resources") version (dgtVersion)
    id("dev.deftu.gradle.tools.minecraft.loom") version (dgtVersion)
    id("dev.deftu.gradle.tools.github-publishing") version (dgtVersion)
    id("dev.deftu.gradle.tools.minecraft.releases") version (dgtVersion)
}

loom {
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
    }
}

toolkit.useDevAuth()


repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.sk1er.club/repository/maven-public/")
    maven("https://repo.sk1er.club/repository/maven-releases/")
    maven("https://repo.spongepowered.org/maven/")
    maven("https://maven.teamresourceful.com/repository/maven-public/")
}

dependencies {
    implementation(shade("gg.essential:elementa-${mcData.versionStr}-${mcData.loader.name}:636") {
        isTransitive = false
    })
    implementation(shade("gg.essential:universalcraft-${mcData.versionStr}-${mcData.loader.name}:323") {
        isTransitive = false
    })
    implementation(shade("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        isTransitive = false
    })
    implementation(shade("com.jagrosh:DiscordIPC:0.5.3") {
        exclude(module = "log4j")
        because("Different version conflicts with Minecraft's Log4J")
        exclude(module = "gson")
        because("Different version conflicts with Minecraft's Gson")
    })

    implementation(kotlin("stdlib"))
}

tasks {
    fatJar {
        relocate("com.jagrosh.discordipc", "gay.j10a1n15.sillygames.deps.discordipc")
    }
}
