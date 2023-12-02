// For `KotlinCompile` task below

plugins {
    kotlin("jvm") version "1.9.21" // Kotlin version to use
    application // Application plugin.
}

group = "com.aroxoft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
    implementation("com.github.ajalt.mordant:mordant:2.2.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}
