import java.io.File
import java.util.concurrent.ConcurrentHashMap
import org.apache.bcel.classfile.ClassParser

class JavaStaticAnalyzer(
    private val sourceDirs: List<String>,
    private val classpath: List<String>,
    private val excludePatterns: List<Regex> = emptyList(),
) {
    val packagePattern = Regex("package\\s+([a-zA-Z0-9_.]+)")
    val classPattern = Regex("(public|private|protected)?\\s*(class|interface|enum)\\s+([a-zA-Z0-9_]+)")
    val importPattern = Regex("import\\s+([a-zA-Z0-9_.]+)(\\s*;|\\.\\*\\s*;)")
    val referencePattern = Regex("\\b([A-Z][a-zA-Z0-9_]*)\\b")
    val annotationPattern = Regex("@([A-Z][a-zA-Z0-9_]*)")

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
        collectAllClasses()
        findReferences()
        return computeUnusedClasses()
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
        try {
            val classParser = ClassParser(file)
            val javaClass = classParser.parse()
            val className = javaClass.className
            if(excludePatterns.none { it.matches(className) }) {
                allClasses.add(className)
            }
        } catch (e: Exception) {
            println("I'm sorry, there seems to be a problem processing your class file: $file: ${e.message}")
        }
    }

    fun processJavaFile(file: String) {
        try {
            val text = File(file).readText()
            val packageMatch = packagePattern.find(text)
            val packageName = packageMatch?.groups?.get(1)?.value ?: ""

            classPattern.findAll(text).forEach { match ->
                val className = match.groups[3]?.value ?: ""
                val fullClassName = if (packageName.isNotEmpty()) "$packageName.$className" else className
                if (excludePatterns.none { it.matches(fullClassName) }) {
                    allClasses.add(fullClassName)
                }
            }
        } catch (e: Exception) {
            println("I'm sorry, there seems to be a problem processing your Java file: $file: ${e.message}")
        }

    }

    fun findReferences() {
        sourceDirs.map{ File(it)}.forEach { dir ->
            if (dir.isFile) {
                dir.walkTopDown()
                    .filter { it.extension == "java" || it.extension == "class" }
                    .forEach { file ->
                        when (file.extension) {
                            "java" -> findReferencesInJavaFile(file.absolutePath)
                            "class" -> findReferencesInClassFile(file.absolutePath)
                        }
                    }
            }
        }
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
    fun isAllClassesEmpty(): Boolean {
        return allClasses.isEmpty()
    }

    fun isEntryPoint(className: String): Boolean {
        return entryPoints.any { className.contains(it) }
    }

    fun computeUnusedClasses() : Set<String> {
        return allClasses.filter { className -> //todo do I want a size print statement?
            !referenceClasses.contains(className) && !isEntryPoint(className)
        }.toSet()
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

fun main() {
    val sourceDirs = listOf("src/test/resources/interview-homework-input")
    val classpath = listOf("lib/some-library.jar")
    val analyzer = JavaStaticAnalyzer(
        sourceDirs = sourceDirs,
        classpath = classpath
    )
    analyzer.collectAllClasses()
    val unusedClasses = analyzer.analyze()
    unusedClasses.sorted().forEach { println(it) }
}