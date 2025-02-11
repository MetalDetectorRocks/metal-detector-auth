package rocks.metaldetector.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

  @Bean
  @Throws(Exception::class)
  fun actuatorSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
    return http
      .csrf { it.disable() }
      .sessionManagement { it.sessionCreationPolicy(STATELESS) }
      .securityMatcher("/actuator/**")
      .authorizeHttpRequests {
        it.requestMatchers("/**").permitAll()
      }
      .build()
  }

  @Bean
  fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
    return BCryptPasswordEncoder()
  }
}
