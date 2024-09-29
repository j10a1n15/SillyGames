plugins {
    java
    kotlin("jvm") version ("2.0.0")
    val dgtVersion = "2.9.0"
    id("dev.deftu.gradle.tools") version (dgtVersion)
    id("dev.deftu.gradle.tools.shadow") version (dgtVersion)
    id("dev.deftu.gradle.tools.bloom") version (dgtVersion)
    id("dev.deftu.gradle.tools.resources") version (dgtVersion)
    id("dev.deftu.gradle.tools.minecraft.loom") version (dgtVersion)
    id("dev.deftu.gradle.tools.minecraft.releases") version (dgtVersion)
}

toolkitLoomHelper.useDevAuth()

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.sk1er.club/repository/maven-public/")
    maven("https://repo.sk1er.club/repository/maven-releases/")
    maven("https://repo.spongepowered.org/maven/")
    maven("https://maven.teamresourceful.com/repository/maven-public/")
    maven("https://repo.essential.gg/repository/maven-public")
}

val vigilanceVersion = "306"
val ucVersion = "363"

dependencies {
    implementation(shade("gg.essential:elementa-${mcData.version}-${mcData.loader.friendlyString}:636") {
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

    implementation("gg.essential:vigilance:$vigilanceVersion")
    modImplementation("gg.essential:universalcraft-1.8.9-forge:$ucVersion")

    modImplementation(include("gg.essential:universalcraft-1.8.9-forge:$ucVersion")!!)
}

tasks {
    fatJar {
        relocate("com.jagrosh.discordipc", "gay.j10a1n15.sillygames.deps.discordipc")
        relocate("gg.essential.vigilance", "gay.j10a1n15.sillygames.deps.vigilance")
        relocate("gg.essential.elementa", "gay.j10a1n15.sillygames.deps.elementa")
        relocate("gg.essential.universalcraft", "gay.j10a1n15.sillygames.deps.universalcraft")
    }
}
