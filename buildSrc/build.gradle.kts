plugins {
    kotlin("jvm") version "2.1.10"
    java
    `java-gradle-plugin`
    `maven-publish`
    pmd
}

group = "com.tazzledazzle"
version = "1.0-SNAPSHOT"
val kotestVersion = "5.9.0"
repositories {
    mavenCentral()
    maven ("https://oss.sonatype.org/content/repositories/snapshots/" )
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
    implementation(kotlin("stdlib"))
    // ASM
    implementation("org.ow2.asm:asm:9.5")
    implementation("org.ow2.asm:asm-commons:9.5")

    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
}

