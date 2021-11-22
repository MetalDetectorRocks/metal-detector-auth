package com.metaldetector.auth

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles("prod")
@Import(IntegrationTestConfig::class)
@TestPropertySource(locations = ["classpath:integrationtest.properties"])
class MetalDetectorAuthApplicationProdIT : FunSpec() {

  init {
    test("applicationContext should load on prod profile") {
      true shouldBe true
    }
  }
}
