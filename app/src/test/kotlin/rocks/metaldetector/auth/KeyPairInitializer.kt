package rocks.metaldetector.auth

import com.nimbusds.jose.util.Base64
import java.security.KeyPairGenerator
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

class KeyPairInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

  override fun initialize(applicationContext: ConfigurableApplicationContext) {
    val keyGenerator = KeyPairGenerator.getInstance("RSA")
    keyGenerator.initialize(2048)
    val keyPair = keyGenerator.generateKeyPair()
    val base64privateKey = Base64.encode(keyPair.private.encoded)
    val base64publicKey = Base64.encode(keyPair.public.encoded)
    TestPropertyValues.of(
        "security.authorization-server-private-key=$base64privateKey",
        "security.authorization-server-public-key=$base64publicKey"
    ).applyTo(applicationContext.environment)
  }
}
