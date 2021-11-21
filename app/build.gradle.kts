plugins {
  id("org.springframework.boot")
}

springBoot {
  mainClass.set("rocks.metaldetector.oauthservice.MetalOAuthServiceApplication")
}

tasks {
  bootJar {
    archiveClassifier.set("boot")
    enabled = true
  }

  jar {
    enabled = false
  }
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}")

  implementation("org.springframework.boot:spring-boot-starter-web:${libs.versions.springBoot.get()}")
  implementation("org.springframework.boot:spring-boot-starter-security:${libs.versions.springBoot.get()}")
  implementation("org.springframework.boot:spring-boot-starter-actuator:${libs.versions.springBoot.get()}")
  implementation("org.springframework.security:spring-security-oauth2-authorization-server:${libs.versions.springSecurityAuthorization.get()}")

  developmentOnly("org.springframework.boot:spring-boot-devtools:${libs.versions.springBoot.get()}")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:${libs.versions.springBoot.get()}")
}

description = "app"
