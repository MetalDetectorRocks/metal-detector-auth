package com.metaldetector.auth.util

import com.metaldetector.auth.properties.ClientConfigurationProperties
import com.metaldetector.auth.properties.ClientProperties
import io.kotest.core.spec.style.FunSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository

class DatabaseInitializerTest : FunSpec({

  lateinit var underTest: DatabaseInitializer

  beforeTest {
    val registeredClientRepository = mockk<RegisteredClientRepository>(relaxed = true)
    val clientConfigurationProperties = ClientConfigurationProperties()

    clientConfigurationProperties["userClient"] = ClientProperties().apply {
      this.clientId = "userId"
      this.clientSecret = "userSecret"
      this.scope = "user"
    }

    clientConfigurationProperties["adminClient"] = ClientProperties().apply {
      this.clientId = "adminId"
      this.clientSecret = "adminSecret"
      this.scope = "admin"
    }

    underTest = DatabaseInitializer().apply {
      this.registeredClientRepository = registeredClientRepository
      this.clientConfigurationProperties = clientConfigurationProperties
    }
  }

  test("should search for every client in clientRepository") {
    // given
    every { underTest.registeredClientRepository.findByClientId(any()) } returns mockk()

    // when
    underTest.run(mockk())

    // then
    verify(exactly = 2) { underTest.registeredClientRepository.findByClientId(any()) }
  }

  test("should save every client that does not yet exist") {
    // given
    every { underTest.registeredClientRepository.findByClientId(any()) } returnsMany listOf(null, mockk())

    // when
    underTest.run(mockk())

    // then
    verify(exactly = 1) { underTest.registeredClientRepository.save(any()) }
  }
})