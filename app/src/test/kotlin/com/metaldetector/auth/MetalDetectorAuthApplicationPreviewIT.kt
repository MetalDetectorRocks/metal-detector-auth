package com.metaldetector.auth

import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("preview")
class MetalDetectorAuthApplicationPreviewIT : FunSpec(), WithIntegrationTestConfig {

  @MockBean
  lateinit var jwkSource: JWKSource<SecurityContext>

  init {
    test("applicationContext should load on preview profile") {
      true shouldBe true
    }
  }
}
