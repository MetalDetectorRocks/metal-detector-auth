package rocks.metaldetector.auth.util

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS
import org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import rocks.metaldetector.auth.properties.ClientConfigurationProperties

class DatabaseInitializerTest : FunSpec({

  lateinit var underTest: DatabaseInitializer

  beforeTest {
    val registeredClientRepository = mockk<RegisteredClientRepository>(relaxed = true)
    val clientConfigurationProperties = ClientConfigurationProperties()
    val bcryptPasswordEncoder = mockk<BCryptPasswordEncoder>(relaxed = true)

    clientConfigurationProperties["userClient"] = OAuth2AuthorizationServerProperties.Client().apply {
      this.registration.clientId = "userId"
      this.registration.clientSecret = "userId"
      this.registration.scopes = setOf("read")
    }

    clientConfigurationProperties["adminClient"] = OAuth2AuthorizationServerProperties.Client().apply {
      this.registration.clientId = "adminId"
      this.registration.clientSecret = "adminSecret"
      this.registration.scopes = setOf("read", "write")
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
    val clientSecret = underTest.clientConfigurationProperties["adminClient"]!!.registration.clientSecret
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
        it.clientId shouldBe underTest.clientConfigurationProperties["adminClient"]?.registration?.clientId
        it.clientSecret shouldBe "secret"
        it.clientAuthenticationMethods shouldBe setOf(CLIENT_SECRET_BASIC)
        it.authorizationGrantTypes shouldBe setOf(CLIENT_CREDENTIALS)
        it.scopes shouldBe underTest.clientConfigurationProperties["adminClient"]?.registration?.scopes
      })
    }
    verify(exactly = 1) {
      underTest.registeredClientRepository.save(withArg {
        it.id shouldBe "userClient"
        it.clientId shouldBe underTest.clientConfigurationProperties["userClient"]?.registration?.clientId
        it.clientSecret shouldBe "secret"
        it.clientAuthenticationMethods shouldBe setOf(CLIENT_SECRET_BASIC)
        it.authorizationGrantTypes shouldBe setOf(CLIENT_CREDENTIALS)
        it.scopes shouldBe underTest.clientConfigurationProperties["userClient"]?.registration?.scopes
      })
    }
  }

  test("initializer is nullsafe") {
    // given
    underTest.clientConfigurationProperties["userClient"]?.registration?.scopes = null
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
