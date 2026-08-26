import org.jetbrains.kotlin.gradle.utils.extendsFrom

plugins {
    id("com.gradleup.shadow")
}

repositories {
    // KFF
    maven {
        name = "Kotlin for Forge"
        setUrl("https://thedarkcolour.github.io/KotlinForForge/")
    }
    maven {
        name = "NeoForged"
        url = uri("https://maven.neoforged.net/releases")
    }
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

val common: Configuration by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}
val shadowCommon: Configuration by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}
val developmentNeoForge: Configuration by configurations.getting

configurations {

    compileOnly.configure { extendsFrom(common) }
    runtimeOnly.configure { extendsFrom(common) }
    developmentNeoForge.extendsFrom(common)
}

dependencies {
    neoForge("net.neoforged:neoforge:${rootProject.property("neoforge_version")}")

    // Architectury API. This is optional, and you can comment it out if you don't need it.
    modImplementation("dev.architectury:architectury-neoforge:${rootProject.property("architectury_api_version").toString()}")

    common(project(":common", "namedElements")) {
        isTransitive = false
    }

    shadowCommon(project(":common", "transformProductionNeoForge")){
        isTransitive = false
    }

    implementation("thedarkcolour:kotlinforforge-neoforge:${rootProject.property("kotlin_for_forge_version")}")
}

tasks.processResources {
    inputs.property ("version", project.version)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(
            mutableMapOf(
                "version" to inputs.properties["version"],
                "mod_id" to rootProject.property("mod_id"),
                "mod_name" to rootProject.property("mod_name"),
                "architectury" to rootProject.property("architectury_api_version"),
                "minecraft_version" to rootProject.property("minecraft_version"),
                "kff" to rootProject.property("kotlin_for_forge_version")
            )
        )
    }
}

tasks.shadowJar {
    configurations = listOf(project.configurations.named("shadowBundle").get())
    archiveClassifier.set("dev-shadow")
}

tasks.remapJar {
    inputFile.set(tasks.shadowJar.flatMap { it.archiveFile })
}