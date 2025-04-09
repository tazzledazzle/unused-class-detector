plugins {
    kotlin("jvm") version "2.1.10"
    id("org.jetbrains.intellij.platform") version "2.5.0"
}

group = "com.tazzledazzle"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/ij/intellij-plugin-service") // IntelliJ IDEA plugin repository
    mavenCentral()

    intellijPlatform {
    localPlatformArtifacts()

//    version = "2023.1" // Specify the IntelliJ IDEA version
//    plugins = ["java", "Kotlin"] // Add required plugins
    }
}

val kotestVersion = "5.9.0"
dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
//    implementation("com.jetbrains.intellij.platform:analysis-api:2019.1.4")
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")

    intellijPlatform {
        intellijIdeaUltimate("2024.3.5")
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}