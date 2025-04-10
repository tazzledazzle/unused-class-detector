plugins {
    kotlin("jvm") version "2.1.10"
//    id("org.jetbrains.intellij.platform") version "2.5.0" // Use the latest 2.x version
    id("org.jetbrains.intellij") version "1.17.3" // Check latest version at https://plugins.jetbrains.com/plugin/8327-gradle-intellij-plugin
}

group = "com.tazzledazzle"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://www.jetbrains.com/intellij-repository/releases") // IntelliJ IDEA plugin repository
    mavenCentral()
    maven ("https://oss.sonatype.org/content/repositories/snapshots/" )
//    intellijPlatform {
//        defaultRepositories()
//    }
}
// Configure IntelliJ Platform plugin
intellij {
    version.set("2024.1") // Use desired IntelliJ IDEA version
    type.set("IC")        // IC for IntelliJ IDEA Community, IU for Ultimate
    plugins.set(listOf("java", "Kotlin"))
}

tasks {
    patchPluginXml {
        sinceBuild.set("241")
        untilBuild.set("241.*")
    }
}
val kotestVersion = "5.9.0"
dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // IntelliJ IDEA dependencies
//    implementation("com.jetbrains.intellij.idea:ideaIC:2024.3")
//    intellijPlatform {
//        intellijIdeaCommunity("2024.1") // Specify the target IntelliJ IDEA version
//    }
    implementation("com.intellij:openapi:7.0.3")
//    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.21")
//    implementation("org.jetbrains.kotlin:kotlin-ide-common:2.1.10")

    // Maven dependencies
    implementation ("org.apache.maven.shared:maven-dependency-analyzer:1.15.1")
    implementation ("org.apache.maven:maven-project:2.2.1")
    implementation ("org.apache.maven:maven-core:3.9.6")
    implementation ("org.apache.bcel:bcel:6.6.1")
    implementation ("org.eclipse.aether:aether-api:1.1.0")
    implementation ("org.eclipse.aether:aether-impl:1.1.0")
    implementation ("org.eclipse.aether:aether-connector-basic:1.1.0")
    implementation ("org.eclipse.aether:aether-transport-file:1.1.0")
    implementation ("org.eclipse.aether:aether-transport-http:1.1.0")
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
