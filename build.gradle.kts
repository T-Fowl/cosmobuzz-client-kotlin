import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    kotlin("jvm") version "1.5.30"
    id("org.jetbrains.dokka") version "1.5.0"
    `maven-publish`
    signing
}

group = "com.tfowl.cosmobuzz"
version = "1.0.0"
description = "Simple kotlin client for cosmobuzz.net room management"

repositories {
    mavenCentral()
}

dependencies {
    /*
        TODO: Use gradle catalog when dependabot supports it
     */

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("com.tfowl.socketio:socket-io-coroutines:1.0.1")

    testImplementation(kotlin("test"))
    testImplementation("app.cash.turbine:turbine:0.6.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1")
}


tasks.withType<Test>().all {
    useJUnitPlatform()
    testLogging {
        events(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
        showStandardStreams = true
        exceptionFormat = TestExceptionFormat.FULL
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
}


tasks.withType<Jar>().configureEach {
    manifest {
        attributes("Automatic-Module-Name" to project.name)
    }
}

val sourcesJar = tasks.register<Jar>("sourcesJar") {
    from(sourceSets["main"].allSource)
    archiveClassifier.set("sources")
}

val javadocJar = tasks.register<Jar>("javadocJar") {
    from(tasks.dokkaJavadoc.get().outputs)
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
            artifact(sourcesJar)
            artifact(javadocJar)

            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/T-Fowl/cosmobuzz-client-kotlin")

                licenses {
                    license {
                        name.set("The MIT License (MIT)")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("T-Fowl")
                        name.set("Thomas Fowler")
                        email.set("thomasjamesfowler97@gmail.com")
                        url.set("https://github.com/T-Fowl")
                    }
                }
                scm {
                    url.set("https://github.com/T-Fowl/cosmobuzz-client-kotlin.git")
                    connection.set("scm:git:https://github.com/T-Fowl/cosmobuzz-client-kotlin.git")
                    developerConnection.set("scm:git:https://github.com/T-Fowl/cosmobuzz-client-kotlin.git")
                }
            }
        }
    }

    repositories {
        maven {
            name = "mavencentral"
            url = URI.create("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("SONATYPE_NEXUS_USERNAME")
                password = System.getenv("SONATYPE_NEXUS_PASSWORD")
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}
