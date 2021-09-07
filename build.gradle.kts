import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
}

group = "com.tfowl.cosmobuzz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("com.tfowl.socketio:socket-io-coroutines:1.0.1")

    testImplementation(kotlin("test"))
    testImplementation("app.cash.turbine:turbine:0.6.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}