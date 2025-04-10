package com.tazzledazzle

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

class UnusedClassesTask : DefaultTask() {

    @get:InputFiles
    abstract var sourceDirectories: ConfigurableFileCollection

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun detectUnusedClasses() {
        // collect all java classes
        val allClasses = findAllClasses()
        logger.lifecycle("Found ${allClasses.size} classes")

        // collect all classes and analyze class dependencies that are used
        val usedClasses = findUsedClasses(allClasses)
        logger.lifecycle("Found ${usedClasses.size} used classes")

        // find unused classes
        val unusedClasses = allClasses - usedClasses
        logger.lifecycle("Found ${unusedClasses.size} unused classes")

        // output results
        val outputFile = outputFile.get().asFile
        outputFile.parentFile.mkdirs()
        outputFile.writeText(unusedClasses.joinToString("\n"))

    }

    fun findAllClasses(): Set<String> {
        val classes = mutableSetOf<String>()
        sourceDirectories.files.forEach { dir ->
            dir.walkTopDown().forEach { file ->
                if (file.extension == "class" || file.extension == "java") {
                    classes.add(file.nameWithoutExtension)
                }
            }
        }
        return classes
    }

    fun findUsedClasses(allClasses: Set<String>): Set<String> {
        val usedClasses = ConcurrentHashMap.newKeySet<String>()

        val classFiles = project.files(project.tasks.findByName("compileJava")?.outputs?.files ?: emptyList<File>())
        classFiles.asFileTree.matching { include("**/*.class") }.forEach { file ->
            try {
                val reader = ClassReader(file.inputStream())
                val visitor = ClassDependencyVisitor(usedClasses)
                reader.accept(visitor, ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)
            }
        }

        return usedClasses
    }
}