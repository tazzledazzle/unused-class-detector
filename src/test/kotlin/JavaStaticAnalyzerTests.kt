import com.tazzledazzle.JavaStaticAnalyzer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import java.io.File

class JavaStaticAnalyzerTests: StringSpec({

    lateinit var analyzer: JavaStaticAnalyzer
    lateinit var sourceDir: File
    lateinit var classpathDir: File

    beforeTest {
        // Setup code if needed
        val tempDir = kotlin.io.path.createTempDirectory()
        sourceDir = File(tempDir.toFile(),"src").apply { mkdirs() }
        classpathDir = File(tempDir.toFile(),"classpath").apply { mkdirs() }
        analyzer = JavaStaticAnalyzer(
            sourceDirs = listOf(sourceDir.absolutePath),
            classpath = listOf(classpathDir.absolutePath)
        )
    }
   "collectAllClasses should collect all Java and class files" {
         // Given
        val javaFile = File(sourceDir, "TestClass.java").apply {
             writeText(
                 """
                     package com.example;
                     public class TestClass {}
                 """.trimIndent()
             )
            File(sourceDir, "TestClass.class").apply {
                createNewFile()
            }
         }

         // When
         analyzer.collectAllClasses()

         // Then
         // Check that the classes are collected correctly
         assert(!analyzer.isAllClassesEmpty())
   }

    "findReferences should detect class references in Java files" {
        // Given
        val sourceDirs = listOf("src/test/resources/interview-homework-input")
        val classpath = listOf("lib/some-library.jar")
        val analyzer = JavaStaticAnalyzer(
            sourceDirs = sourceDirs,
            classpath = classpath
        )

        // When
        analyzer.collectAllClasses()
        analyzer.findReferences()

        // Then
        // Check that the references are detected correctly
        assert(analyzer.checkReferenceClasses().isNotEmpty())
    }

    "computeUnusedClasses should return classes that are not referenced" {
        // Given
        File(sourceDir, "Unused.java").apply {
            writeText(
                """
                    package com.example;
                    public class Unused {}
                """.trimIndent()
            )
        }
        File(sourceDir, "Used.java").apply {
            writeText(
                """
                    package com.example;
                    public class Used {}
                """.trimIndent()
            )
        }
        File(sourceDir, "Using.java").apply {
            writeText(
                """
                    package com.example;
                    public class Using {
                        Used used = new Used();
                    }
                """.trimIndent()
            )
        }
        // When
        analyzer.collectAllClasses()
        analyzer.findReferences()
        val unusedClasses = analyzer.computeUnusedClasses()

        // Then
        // Check that the unused classes are computed correctly
        assert(unusedClasses.isNotEmpty())
        unusedClasses.shouldContain ("com.example.Unused")
        unusedClasses.shouldNotContain ("com.example.Used")
    }

    "analyze should be thread-safe" {
        File(sourceDir, "ThreadSafe.java").apply {
            writeText(
                """
                    package com.example;
                    public class ThreadSafeTest {}
                """.trimIndent()
            )
        }
        val threads = List(10) { Thread { analyzer.analyze() } }
        threads.forEach { it.start() }
        threads.forEach { it.join() }

        analyzer.listAllClasses().contains("com.example.ThreadSafeTest").shouldBeTrue()
    }
})