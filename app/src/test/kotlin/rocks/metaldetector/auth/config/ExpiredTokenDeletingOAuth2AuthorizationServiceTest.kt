package rocks.metaldetector.auth.config

import io.kotest.core.spec.style.FunSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.jdbc.core.ConnectionCallback
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository

class ExpiredTokenDeletingOAuth2AuthorizationServiceTest : FunSpec({

  lateinit var underTest: ExpiredTokenDeletingOAuth2AuthorizationService
  val jdbcOperations = mockk<JdbcOperations>(relaxed = true)

  beforeTest {
    every { jdbcOperations.execute(any<ConnectionCallback<Int>>()) } returns 0
    val registeredClientsRepository = mockk<RegisteredClientRepository>()
    underTest = ExpiredTokenDeletingOAuth2AuthorizationService(jdbcOperations, registeredClientsRepository)
  }

  test("jdbcOperations should be called with expected query") {
    // when
    underTest.removeExpiredAuthorizations()

    // then
    verify(exactly = 1) { jdbcOperations.update(underTest.REMOVE_AUTHORIZATION_SQL) }
  }
})
