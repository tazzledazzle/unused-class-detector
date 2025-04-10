plugins {
    kotlin("jvm") version "2.1.10"
}

group = "com.tazzledazzle"
version = "1.0-SNAPSHOT"
val kotestVersion = "5.9.0"

repositories {
    maven("https://www.jetbrains.com/intellij-repository/releases") // IntelliJ IDEA plugin repository
    mavenCentral()
    maven ("https://oss.sonatype.org/content/repositories/snapshots/" )
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.intellij:openapi:7.0.3")

    // Test
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
