package rocks.metaldetector.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

  @Bean
  @Throws(Exception::class)
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http.authorizeHttpRequests { authorizeRequests ->
      authorizeRequests
          .requestMatchers("/actuator/**").permitAll()
          .anyRequest().authenticated()
    }
    return http.build()
  }

  @Bean
  fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
    return BCryptPasswordEncoder()
  }
}
