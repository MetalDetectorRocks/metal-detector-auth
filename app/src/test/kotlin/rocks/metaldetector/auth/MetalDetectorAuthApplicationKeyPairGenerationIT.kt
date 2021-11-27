package rocks.metaldetector.auth

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(locations = ["classpath:integrationtest.properties"],
    properties = ["security.private-key=", "security.public-key="])
class MetalDetectorAuthApplicationKeyPairGenerationIT : FunSpec() {

  init {
    test("applicationContext should load with generated keypair") {
      true shouldBe true
    }
  }
}
