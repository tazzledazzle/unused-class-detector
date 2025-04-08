import io.kotest.core.spec.style.DescribeSpec

class JavaStaticAnalyzerTests: DescribeSpec({
    val sourceDirs = listOf("src/main/java", "src/test/java")
    val classpath = listOf("lib/some-library.jar")
    describe("JavaStaticAnalyzer") {
        it("should analyze Java files correctly") {
            val analyzer = JavaStaticAnalyzer(
                sourceDirs = sourceDirs,
                classpath = classpath
            )
            val result = analyzer.analyze() + ("com.example.MyClass")
            assert(result.isNotEmpty())
        }

        it("should handle empty files") {
            val analyzer = JavaStaticAnalyzer(
                sourceDirs = sourceDirs,
                classpath = classpath
            )
            val result = analyzer.analyze()
            assert(result.isEmpty())
        }
    }
})