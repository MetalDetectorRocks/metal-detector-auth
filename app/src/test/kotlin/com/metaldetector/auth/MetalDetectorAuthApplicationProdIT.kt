package com.metaldetector.auth

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("prod")
class MetalDetectorAuthApplicationProdIT : FunSpec(), WithIntegrationTestConfig {

  init {
    test("applicationContext should load on prod profile") {
      true shouldBe true
    }
  }
}
