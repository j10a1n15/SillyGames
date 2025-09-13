import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    java
    kotlin("jvm") version ("2.2.0")
    alias(libs.plugins.loom)
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.spongepowered.org/maven/")
    maven("https://maven.teamresourceful.com/repository/maven-public/")
    maven("https://repo.essential.gg/repository/maven-public")
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

val vigilanceVersion = "306"
val ucVersion = "427"

dependencies {
    minecraft("com.mojang:minecraft:${libs.versions.minecraft.get()}")
    mappings(loom.officialMojangMappings())

    modImplementation(libs.fabricLoader)
    modImplementation(libs.fabricApi)
    modImplementation(libs.fabricKt)

    modImplementation(libs.devauth)

    implementation("gg.essential:elementa:710") {
        isTransitive = false
    }
    implementation("com.jagrosh:DiscordIPC:0.5.3") {
        exclude(module = "log4j")
        because("Different version conflicts with Minecraft's Log4J")
        exclude(module = "gson")
        because("Different version conflicts with Minecraft's Gson")
    }

    implementation("gg.essential:vigilance:$vigilanceVersion")

    modImplementation(include("gg.essential:universalcraft-1.21.7-fabric:$ucVersion")!!)
}


loom {
    runs {
        getByName("client") {
            property("devauth.configDir", rootProject.file(".devauth").absolutePath)
            vmArgs("-Ddevauth.enabled=true")
        }
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        inputs.property("minecraft_version", libs.versions.minecraft.get())
        inputs.property("loader_version", libs.versions.fabricLoader.get())
        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand(
                "version" to project.version,
                "minecraft_version" to libs.versions.minecraft.get(),
                "loader_version" to libs.versions.fabricLoader.get(),
                "kotlin_loader_version" to libs.versions.fabricKt.get(),
            )
        }
    }

    jar {
        from("LICENSE")
    }

    compileKotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }
}


java {
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
}

//tasks.withType<KotlinCompile>().configureEach {
//    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
//}
