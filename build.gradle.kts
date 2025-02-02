

plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    id("com.gradleup.shadow") version "9.0.0-beta6"
}

group = "ir.rezajax"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "ir.rezajax.MainKt"
        )
    }

}

tasks {
    shadowJar {
        // Specify the main class to run
        manifest {
            attributes["Main-Class"] = "ir.rezajax.MainKt" // Replace with your main class path
        }
        archiveBaseName.set("my-shadow-jar") // Name of the JAR file
        archiveVersion.set("1.0.0") // Set the version if necessary
        mergeServiceFiles() // Optional: Merges service files from dependencies
    }
}

dependencies {

    implementation("io.netty:netty-all:4.1.115.Final")

    implementation(project(":module"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}