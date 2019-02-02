import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.10"
    id("com.github.johnrengelman.shadow") version "2.0.4"
}

group = "arbybot"
version = "2.0"

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://libraries.minecraft.net")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.javacord:javacord:3.0.0")
    implementation("com.vdurmont:emoji-java:4.0.0")
    implementation("com.mojang:brigadier:1.0.15")
}

tasks.withType<ShadowJar> {
    manifest {
        attributes["Main-Class"] = "arbybot.MainKt"
    }
}


configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
