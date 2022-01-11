package rocks.metaldetector.auth.util

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS
import org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC
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
      this.scopes = setOf("read")
    }

    clientConfigurationProperties["adminClient"] = ClientProperties().apply {
      this.clientId = "adminId"
      this.clientSecret = "adminSecret"
      this.scopes = setOf("read", "write")
    }

    underTest = DatabaseInitializer(registeredClientRepository, clientConfigurationProperties, bcryptPasswordEncoder)
  }

  test("should search for every client in clientRepository") {
    // given
    every { underTest.registeredClientRepository.findByClientId(any()) } returns mockk()

    // when
    underTest.run { run(mockk()) }

    // then
    verify(exactly = 2) { underTest.registeredClientRepository.findByClientId(any()) }
  }

  test("should encrypt every client secret") {
    // given
    val clientSecret = underTest.clientConfigurationProperties["adminClient"]!!.clientSecret
    every { underTest.registeredClientRepository.findByClientId(any()) } returnsMany listOf(null, mockk())

    // when
    underTest.run { run(mockk()) }

    // then
    verify(exactly = 1) { underTest.bCryptPasswordEncoder.encode(clientSecret) }
  }

  test("should save every client that does not yet exist") {
    // given
    every { underTest.registeredClientRepository.findByClientId(any()) } returnsMany listOf(null, mockk())

    // when
    underTest.run { run(mockk()) }

    // then
    verify(exactly = 1) { underTest.registeredClientRepository.save(any()) }
  }

  test("properties are set correctly") {
    // given
    every { underTest.registeredClientRepository.findByClientId(any()) } returnsMany listOf(null, null)
    every { underTest.bCryptPasswordEncoder.encode(any()) } returns "secret"

    // when
    underTest.run { run(mockk()) }

    // then
    verify(exactly = 1) {
      underTest.registeredClientRepository.save(withArg {
        it.id shouldBe "adminClient"
        it.clientId shouldBe underTest.clientConfigurationProperties["adminClient"]?.clientId
        it.clientSecret shouldBe "secret"
        it.clientAuthenticationMethods shouldBe setOf(CLIENT_SECRET_BASIC)
        it.authorizationGrantTypes shouldBe setOf(CLIENT_CREDENTIALS)
        it.scopes shouldBe underTest.clientConfigurationProperties["adminClient"]?.scopes
      })
    }
    verify(exactly = 1) {
      underTest.registeredClientRepository.save(withArg {
        it.id shouldBe "userClient"
        it.clientId shouldBe underTest.clientConfigurationProperties["userClient"]?.clientId
        it.clientSecret shouldBe "secret"
        it.clientAuthenticationMethods shouldBe setOf(CLIENT_SECRET_BASIC)
        it.authorizationGrantTypes shouldBe setOf(CLIENT_CREDENTIALS)
        it.scopes shouldBe underTest.clientConfigurationProperties["userClient"]?.scopes
      })
    }
  }

  test("initializer is nullsafe") {
    // given
    underTest.clientConfigurationProperties["userClient"]?.scopes = null
    underTest.clientConfigurationProperties.remove("adminClient")
    every { underTest.registeredClientRepository.findByClientId(any()) } returns null

    // when
    underTest.run { run(mockk()) }

    // then
    verify(exactly = 1) {
      underTest.registeredClientRepository.save(withArg {
        it.scopes shouldBe emptySet()
      })
    }
  }
})
