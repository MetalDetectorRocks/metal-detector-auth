package rocks.metaldetector.auth.util

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import rocks.metaldetector.auth.properties.ClientConfigurationProperties

@Component
class DatabaseInitializer(val registeredClientRepository: RegisteredClientRepository,
                          val clientConfigurationProperties: ClientConfigurationProperties) : ApplicationRunner {

  @Transactional
  override fun run(args: ApplicationArguments) {
    clientConfigurationProperties.entries.forEach {
      val id = it.key
      val clientProperties = it.value
      if (registeredClientRepository.findByClientId(clientProperties.clientId) == null) {
        val registeredClient = RegisteredClient.withId(id)
            .clientId(clientProperties.clientId)
            .clientSecret(clientProperties.clientSecret)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .scope(clientProperties.scope)
            .build()
        registeredClientRepository.save(registeredClient)
      }
    }
  }
}
