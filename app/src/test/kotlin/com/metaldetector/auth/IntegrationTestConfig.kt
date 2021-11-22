package com.metaldetector.auth

import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean

@TestConfiguration
class IntegrationTestConfig {

  @MockBean
  lateinit var jwkSource: JWKSource<SecurityContext>
}
