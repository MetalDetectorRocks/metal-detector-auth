package com.metaldetector.auth

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("preview")
class MetalDetectorAuthApplicationPreviewIT : FunSpec(), WithIntegrationTestConfig {

  init {
    test("applicationContext should load on preview profile") {
      true shouldBe true
    }
  }
}
