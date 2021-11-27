package rocks.metaldetector.auth.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "oauth-clients")
class ClientConfigurationProperties : HashMap<String, ClientProperties>()
