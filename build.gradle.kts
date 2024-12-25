plugins {
    kotlin("jvm") version "2.1.0"
    application
}

application {
    mainClass.set("ir.rezajax.MainKt")
}

tasks.jar {
    doFirst{
        println("Creating JAR file...")
        println(configurations.runtimeClasspath.get().forEach{println(it)})
    }
    manifest {
        attributes["Main-Class"] = "ir.rezajax.MainKt"
    }
    println(configurations.runtimeClasspath.get())
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

}


group = "ir.rezajax"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}