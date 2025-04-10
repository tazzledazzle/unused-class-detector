// src/main/kotlin/com/example/UnusedClassesPlugin.kt
package com.tazzledazzle

import org.gradle.api.Plugin
import org.gradle.api.Project

class UnusedClassesPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Register the task
        val extension = project.extensions.create("unusedClasses", UnusedClassesExtension::class.java)

        project.tasks.register("detectUnusedClasses", UnusedClassesTask::class.java) { task ->
            task.sourceDirectories = (project.files(extension.sourceDirectories))
            task.outputFile.set(project.layout.buildDirectory.file("reports/unused-classes.txt"))
        }
    }
}

// Extension to configure the plugin
open class UnusedClassesExtension {
    val sourceDirectories = mutableListOf("src/main/java")
}