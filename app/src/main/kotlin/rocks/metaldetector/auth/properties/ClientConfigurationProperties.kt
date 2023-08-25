package rocks.metaldetector.auth.properties

import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.authorizationserver.client")
class ClientConfigurationProperties : HashMap<String, OAuth2AuthorizationServerProperties.Client>()
