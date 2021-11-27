package rocks.metaldetector.auth.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings
import org.springframework.security.web.SecurityFilterChain

@Configuration
@Import(OAuth2AuthorizationServerConfiguration::class)
class AuthorizationServerConfig {

  val KEY_ID = "metal-detector-auth"

  @Bean
  @Throws(Exception::class)
  fun authServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
    return http.formLogin(Customizer.withDefaults()).build()
  }

  @Bean
  fun jwkSource(@Value("\${security.private-key}") privateKey: String,
                @Value("\${security.public-key}") publicKey: String): JWKSource<SecurityContext> {
    val rsaKey: RSAKey = if (privateKey.isNotBlank() && publicKey.isNotBlank()) {
      loadRsa(privateKey, publicKey)
    }
    else {
      generateRsa()
    }
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
    return ExpiredTokenDeletingOAuth2AuthorizationService(jdbcOperations, registeredClientRepository)
  }

  private fun loadRsa(privateKeyString: String, publicKeyString: String): RSAKey {
    val keyFactory = KeyFactory.getInstance("RSA")
    val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
    val privateKey: PrivateKey = keyFactory.generatePrivate(keySpecPKCS8)

    val keySpecX509 = X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString))
    val publicKey: RSAPublicKey = keyFactory.generatePublic(keySpecX509) as RSAPublicKey

    return RSAKey.Builder(publicKey)
        .privateKey(privateKey)
        .keyID(KEY_ID)
        .build()
  }

  private fun generateRsa(): RSAKey {
    val keyPair: KeyPair = generateRsaKey()
    val publicKey = keyPair.public as RSAPublicKey
    val privateKey = keyPair.private as RSAPrivateKey
    return RSAKey.Builder(publicKey)
        .privateKey(privateKey)
        .keyID(KEY_ID)
        .build()
  }

  private fun generateRsaKey(): KeyPair {
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
    keyPairGenerator.initialize(2048)
    return keyPairGenerator.generateKeyPair()
  }
}
