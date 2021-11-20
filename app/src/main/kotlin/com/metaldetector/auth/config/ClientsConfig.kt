package com.metaldetector.auth.config

import com.metaldetector.auth.properties.ClientConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository

@Configuration
class ClientsConfig {

  @Bean
  fun registeredClientRepository(bCryptPasswordEncoder: BCryptPasswordEncoder, clientConfigurationProperties: ClientConfigurationProperties): RegisteredClientRepository {
    val clients = clientConfigurationProperties.values.map {
      RegisteredClient.withId(it.clientId)
          .clientId(it.clientId)
          .clientSecret(bCryptPasswordEncoder.encode(it.clientSecret))
          .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
          .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
          .scope(it.scope)
          .build()
    }
    return InMemoryRegisteredClientRepository(clients)
  }
}
