import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.ConcurrentHashMap

class JavaStaticAnalyzer(
    private val sourceDirs: List<String>,
    private val classpath: List<String>,
    private val excludePatterns: List<Regex> = emptyList(),
) {
    private val allClasses = ConcurrentHashMap.newKeySet<String>()
    private val referenceClasses = ConcurrentHashMap.newKeySet<String>()
    private val entryPoints = setOf(
        "main",
        "init",
        "contextInitialized",
        "onStartup",
        "configure"
    )

    fun analyze(): Set<String> {
        // Placeholder for static analysis logic
        return emptySet()
    }

    fun collectAllClasses() {
        sourceDirs.map{ File(it)}.forEach { directory ->
            if (directory.isFile) {
                directory.walkTopDown()
                    .filter { it.extension == "java" || it.extension == "class" }
                    .forEach { file ->
                        when (file.extension) {
                            "java" -> processJavaFile(file.absolutePath)
                            "class" -> processClassFile(file.absolutePath)
                        }
                    }
            }
        }
    }

    fun processClassFile(file: String) {
        // Placeholder for processing class files
        // collect all classes
        // check if class is a reference class
        // check if class is an entry point
    }

    fun processJavaFile(file: String) {
        // Placeholder for processing Java files
        // collect all classes
        // check if class is a reference class
        // check if class is an entry point
    }

    fun findReferences() {

    }
    fun findReferencesInJavaFile(file: String) {
        // Placeholder for finding references in Java files
        // check if class is a reference class
        // check if class is an entry point
    }

    fun findReferencesInClassFile(file: String) {
        // Placeholder for finding references in class files
        // check if class is a reference class
        // check if class is an entry point
    }

    fun isEntryPoint(className: String): Boolean {
        return entryPoints.any { className.contains(it) }
    }

    fun computeUnusedClasses() : Set<String> {
        // Placeholder for computing unused classes
        // return allClasses - referenceClasses
        return emptySet()
    }
}

class SpecialCaseHandler {
    fun findAnnotationDrivenClasses(annotationClasses: List<String>): Set<String> {
        // Placeholder for finding annotation-driven classes
        // return allClasses.filter { className ->
        //     annotationClasses.any { annotation -> className.contains(annotation) }
        // }.toSet()
        return emptySet()
    }

    fun findReflectionReferences(patterns: List<String>): Set<String> {
        // Placeholder for finding reflection references
        // return allClasses.filter { className ->
        //     patterns.any { pattern -> className.contains(pattern) }
        // }.toSet()
        return emptySet()
    }

    fun findConfigurationBasedReferences(configFiles: List<String>): Set<String> {
        // Placeholder for finding configuration-based references
        // return allClasses.filter { className ->
        //     configFiles.any { configFile -> className.contains(configFile) }
        // }.toSet()
        return emptySet()
    }
}