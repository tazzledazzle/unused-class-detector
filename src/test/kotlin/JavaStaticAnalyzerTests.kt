import io.kotest.core.spec.style.StringSpec

class JavaStaticAnalyzerTests: StringSpec({
   "collectAllClasses should collect all Java and class files" {
         // Given
         val sourceDirs = listOf("src/test/resources/interview-homework-input")
         val classpath = listOf("lib/some-library.jar")
         val analyzer = JavaStaticAnalyzer(
              sourceDirs = sourceDirs,
              classpath = classpath
         )

         // When
         analyzer.collectAllClasses()

         // Then
         // Check that the classes are collected correctly
         assert(analyzer.isAllClassesEmpty())
   }
})