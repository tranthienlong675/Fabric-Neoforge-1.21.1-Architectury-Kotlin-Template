import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    `maven-publish`
    base

    id("dev.architectury.loom") version "1.17-SNAPSHOT" apply false
    id("architectury-plugin") version "3.5-SNAPSHOT"
    id("com.gradleup.shadow") version "9.4.3" apply false

    id("idea")

    kotlin("jvm") version "2.4.10"
}

architectury {
    minecraft = project.property("minecraft_version").toString()
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "architectury-plugin")
    apply(plugin = "maven-publish")

    base.archivesName.set(rootProject.property("archives_name").toString())
    group = rootProject.property("maven_group").toString()
    version = rootProject.property("mod_version").toString()

    dependencies {
        compileOnly("org.jetbrains.kotlin:kotlin-stdlib")
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
        withSourcesJar()
    }
}

subprojects {
    apply(plugin = "dev.architectury.loom")

    val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")


    dependencies {
        "minecraft"("com.mojang:minecraft:${project.property("minecraft_version")}")
        // The following line declares the mojmap mappings, you may use other mappings as well
        "mappings"(
            loom.officialMojangMappings()
        )
        // The following line declares the yarn mappings you may select this one as well.
        // "mappings"("net.fabricmc:yarn:1.18.2+build.3:v2")
    }
}