package rocks.metaldetector.auth.util

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS
import org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import rocks.metaldetector.auth.properties.ClientConfigurationProperties

@Component
class DatabaseInitializer(val registeredClientRepository: RegisteredClientRepository,
                          val clientConfigurationProperties: ClientConfigurationProperties,
                          val bCryptPasswordEncoder: BCryptPasswordEncoder) : ApplicationRunner {

  @Transactional
  override fun run(args: ApplicationArguments) {
    clientConfigurationProperties.entries.forEach {
      val id = it.key
      val clientProperties = it.value
      if (registeredClientRepository.findByClientId(clientProperties.registration.clientId) == null) {
        val clientBuilder = RegisteredClient.withId(id)
            .clientId(clientProperties.registration.clientId)
            .clientSecret(bCryptPasswordEncoder.encode(clientProperties.registration.clientSecret))
            .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
            .authorizationGrantType(CLIENT_CREDENTIALS)
        clientProperties.registration.scopes?.forEach { scope ->
          clientBuilder.scope(scope)
        }
        registeredClientRepository.save(clientBuilder.build())
      }
    }
  }
}
