package rocks.metaldetector.auth

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(locations = ["classpath:integrationtest.properties"],
    properties = ["security.authorization-server-private-key=", "security.authorization-server-public-key="])
class MetalDetectorAuthApplicationKeyPairGenerationIntegrationTest : FunSpec() {

  init {
    test("applicationContext should load with generated keypair") {
      true shouldBe true
    }
  }
}
