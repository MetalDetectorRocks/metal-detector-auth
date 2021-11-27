package rocks.metaldetector.auth

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles("default")
@ContextConfiguration(initializers = [KeyPairInitializer::class])
@TestPropertySource(locations = ["classpath:integrationtest.properties"])
class MetalDetectorAuthApplicationDefaultIT : FunSpec() {

  init {
    test("applicationContext should load on default profile") {
      true shouldBe true
    }
  }
}
