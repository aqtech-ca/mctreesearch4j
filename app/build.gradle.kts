import org.jetbrains.compose.compose

group = "ca.aqtech"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.4.21-2"
    id("org.jetbrains.compose") version "0.3.0-build146"
    java
    application
}

repositories {
    flatDir {dirs = setOf(file("libs")) } // have to move it into the a library folder
    jcenter()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation( "ca.aqtech:mctreesearch4j:0.0.3" )
    implementation(compose.desktop.currentOs)
    implementation( "ca.aqtech.app:app-1.0-SNAPSHOT" )
    testImplementation(kotlin("test-junit"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.1.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.1.0")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<Test> {
    useJUnitPlatform()
}

/*
compose.desktop {
    application {
        mainClass = "ReversiMainKt"
    }
}
 */
application {
    mainClassName = "MCTSGamesMain.MCTSGamesMainKt"
}

abstract class GreetingTask : DefaultTask() {
    @TaskAction
    fun greet() {
        println("hello from GreetingTask")
    }
}

tasks.register<GreetingTask>("hello")
