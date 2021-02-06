import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "me.john_"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.4.10"
    java
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    flatDir {dirs = setOf(file("libs")) } // have to move it into the a library folder

}
dependencies {
    implementation( "org.example:lib:1.0-SNAPSHOT"  )
}

dependencies {
    implementation( "org.example:lib:1.0-SNAPSHOT" )
    implementation( "de.magoeke.kotlin:connectfour:1.0-SNAPSHOT" )
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