import io.kotest.core.spec.style.FunSpec
import kotlin.test.assertEquals

class AppTest: FunSpec({
    context("App") {
        test("should run successfully") {
            val app = App()
            assertEquals("Hello, World!", app.greet())
        }
    }
})