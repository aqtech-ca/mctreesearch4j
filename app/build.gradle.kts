import org.jetbrains.compose.compose

group = "me.john_"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.5.0"
    id("org.jetbrains.compose") version "0.4.0-build209"
    java
}

repositories {
    flatDir {dirs = setOf(file("libs")) } // have to move it into the a library folder
    jcenter()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    mavenLocal()
    mavenCentral()
}
dependencies {
    implementation( "com.mcts:lib:1.0-SNAPSHOT" )
    implementation(compose.desktop.currentOs)
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

compose.desktop {
    application {
        mainClass = "ReversiMainKt"
    }
}


