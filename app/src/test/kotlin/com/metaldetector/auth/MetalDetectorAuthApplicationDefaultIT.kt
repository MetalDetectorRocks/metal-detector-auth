package com.metaldetector.auth

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("default")
class MetalDetectorAuthApplicationDefaultIT : FunSpec(), WithIntegrationTestConfig {

  init {
    test("applicationContext should load on default profile") {
      true shouldBe true
    }
  }
}
