package rocks.metaldetector.auth.util

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
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
      if (registeredClientRepository.findByClientId(clientProperties.clientId) == null) {
        val clientBuilder = RegisteredClient.withId(id)
            .clientId(clientProperties.clientId)
            .clientSecret(bCryptPasswordEncoder.encode(clientProperties.clientSecret))
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
        clientProperties.scopes?.forEach { scope ->
          clientBuilder.scope(scope)
        }
        registeredClientRepository.save(clientBuilder.build())
      }
    }
  }
}
