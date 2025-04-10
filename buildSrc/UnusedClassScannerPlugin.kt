// buildSrc/src/main/kotlin/UnusedClassScannerPlugin.kt

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

class UnusedClassScannerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("scanUnusedClasses", ScanUnusedClassesTask::class.java) {
            sourceDir = File(project.projectDir, "src/main/java")
            reportFile = File(project.buildDir, "reports/unused-classes.html")
        }
    }
}

abstract class ScanUnusedClassesTask : DefaultTask() {

    @InputDirectory
    lateinit var sourceDir: File

    @OutputFile
    lateinit var reportFile: File

    @TaskAction
    fun scan() {
        val classFiles = sourceDir.walkTopDown().filter { it.extension == "java" }.toList()
        val classNames = mutableSetOf<String>()
        val referencedClasses = mutableSetOf<String>()

        val classPattern = Regex("class\\s+(\\w+)")
        val usagePattern = Regex("\\b(\\w+)\\b")

        classFiles.forEach { file ->
            val content = file.readText()

            classPattern.findAll(content).forEach { match ->
                classNames.add(match.groupValues[1])
            }

            usagePattern.findAll(content).forEach { match ->
                referencedClasses.add(match.groupValues[1])
            }
        }

        val unusedClasses = classNames - referencedClasses

        generateHtmlReport(unusedClasses)
    }

    private fun generateHtmlReport(unusedClasses: Set<String>) {
        reportFile.parentFile.mkdirs()
        reportFile.writeText("""
            <html>
            <head>
                <title>Unused Classes Report</title>
                <style>
                    body { font-family: Arial, sans-serif; }
                    table { border-collapse: collapse; width: 100%; }
                    th, td { border: 1px solid #ddd; padding: 8px; }
                    th { background-color: #f2f2f2; }
                </style>
            </head>
            <body>
                <h2>Unused Classes Report</h2>
                <table>
                    <tr><th>#</th><th>Class Name</th></tr>
                    ${unusedClasses.mapIndexed { index, cls ->
            "<tr><td>${index + 1}</td><td>$cls</td></tr>"
        }.joinToString("\n")}
                </table>
            </body>
            </html>
        """.trimIndent())

        println("Unused classes report generated at: ${reportFile.absolutePath}")
    }
}
