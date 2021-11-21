package com.metaldetector.auth.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import java.util.UUID
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings
import org.springframework.security.web.SecurityFilterChain

@Configuration
@Import(OAuth2AuthorizationServerConfiguration::class)
class AuthorizationServerConfig {

  @Value("\${security.private-key}")
  lateinit var privateKey: String

  @Value("\${security.public-key}")
  lateinit var publicKey: String

  @Bean
  @Throws(Exception::class)
  fun authServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
    return http.formLogin(Customizer.withDefaults()).build()
  }

  @Bean
  fun jwkSource(): JWKSource<SecurityContext> {
    val rsaKey: RSAKey = loadRsa()
    val jwkSet = JWKSet(rsaKey)
    return JWKSource { jwkSelector, _ -> jwkSelector.select(jwkSet) }
  }

  @Bean
  fun providerSettings(@Value("\${security.issuer-uri}") issuerUri: String): ProviderSettings {
    return ProviderSettings.builder()
        .issuer(issuerUri)
        .build()
  }

  @Bean
  fun registeredClientRepository(jdbcOperations: JdbcOperations): RegisteredClientRepository {
    return JdbcRegisteredClientRepository(jdbcOperations)
  }

  @Bean
  fun oAuth2AuthorizationService(jdbcOperations: JdbcOperations, registeredClientRepository: RegisteredClientRepository): OAuth2AuthorizationService {
    return JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository)
  }

  private fun loadRsa(): RSAKey {
    val keyFactory = KeyFactory.getInstance("RSA")
    val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey))
    val privateKey: PrivateKey = keyFactory.generatePrivate(keySpecPKCS8)

    val keySpecX509 = X509EncodedKeySpec(Base64.getDecoder().decode(publicKey))
    val publicKey: RSAPublicKey = keyFactory.generatePublic(keySpecX509) as RSAPublicKey

    return RSAKey.Builder(publicKey)
        .privateKey(privateKey)
        .keyID(UUID.randomUUID().toString())
        .build()
  }
}
