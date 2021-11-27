package rocks.metaldetector.auth.config

import org.springframework.jdbc.core.JdbcOperations
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import rocks.metaldetector.auth.util.logger

class ExpiredTokenDeletingOAuth2AuthorizationService(jdbcOperations: JdbcOperations, registeredClientRepository: RegisteredClientRepository)
  : JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository) {

  val REMOVE_AUTHORIZATION_SQL = "delete from oauth2_authorization where access_token_expires_at < NOW();"

  private val log by logger()

  @Scheduled(cron = "0 0 0 * * *")
  fun removeExpiredAuthorizations() {
    val deletedTokens = jdbcOperations.update(REMOVE_AUTHORIZATION_SQL)
    log.info("Deleted $deletedTokens expired tokens")
  }
}
