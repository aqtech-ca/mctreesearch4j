import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
}
group = "me.john_"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    flatDir {dirs = setOf(file("libs")) }
}
dependencies {
    implementation( "org.example:lib:1.0-SNAPSHOT"  )
    // implementation files("libs/org.example:lib:1.0-SNAPSHOT")
    // implementation files("libs/lib-1.0-SNAPSHOT.jar")
    // fileTree(dir: "libs", include: "*.jar")
    testImplementation(kotlin("test-junit"))
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}