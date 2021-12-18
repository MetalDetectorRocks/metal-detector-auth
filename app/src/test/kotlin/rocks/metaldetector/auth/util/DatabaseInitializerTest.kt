package rocks.metaldetector.auth.util

import io.kotest.core.spec.style.FunSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import rocks.metaldetector.auth.properties.ClientConfigurationProperties
import rocks.metaldetector.auth.properties.ClientProperties

class DatabaseInitializerTest : FunSpec({

  lateinit var underTest: DatabaseInitializer

  beforeTest {
    val registeredClientRepository = mockk<RegisteredClientRepository>(relaxed = true)
    val clientConfigurationProperties = ClientConfigurationProperties()
    val bcryptPasswordEncoder = mockk<BCryptPasswordEncoder>(relaxed = true)

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

    underTest = DatabaseInitializer(registeredClientRepository, clientConfigurationProperties, bcryptPasswordEncoder)
  }

  test("should search for every client in clientRepository") {
    // given
    every { underTest.registeredClientRepository.findByClientId(any()) } returns mockk()

    // when
    underTest.run(mockk())

    // then
    verify(exactly = 2) { underTest.registeredClientRepository.findByClientId(any()) }
  }

  test("should encrypt every client secret") {
    // given
    val clientSecret = underTest.clientConfigurationProperties["adminClient"]!!.clientSecret
    every { underTest.registeredClientRepository.findByClientId(any()) } returnsMany listOf(null, mockk())

    // when
    underTest.run(mockk())

    // then
    verify(exactly = 1) { underTest.bCryptPasswordEncoder.encode(clientSecret) }
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
