import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
  id("org.springframework.boot")
  id("de.europace.docker-publish")
}

dockerPublish {
  organisation.set("metaldetector")
  imageName.set(rootProject.name)
  imageTag.set(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")))
}

springBoot {
  mainClass.set("rocks.metaldetector.auth.MetalDetectorAuthApplicationKt")
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
  implementation("org.springframework.boot:spring-boot-starter-data-jpa:${libs.versions.springBoot.get()}")
  implementation("org.springframework.security:spring-security-oauth2-authorization-server:${libs.versions.springSecurityAuthorization.get()}")

  implementation("org.flywaydb:flyway-core:${libs.versions.flyway.get()}")
  implementation("io.micrometer:micrometer-registry-prometheus:${libs.versions.micrometer.get()}")
  runtimeOnly("org.postgresql:postgresql:${libs.versions.postgresql.get()}")

  developmentOnly("org.springframework.boot:spring-boot-devtools:${libs.versions.springBoot.get()}")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:${libs.versions.springBoot.get()}")

  testImplementation("org.springframework.boot:spring-boot-starter-test:${libs.versions.springBoot.get()}")
  testImplementation("io.kotest:kotest-framework-engine-jvm:${libs.versions.kotest.get()}")
  testImplementation("io.kotest:kotest-property-jvm:${libs.versions.kotest.get()}")
  testImplementation("io.kotest:kotest-runner-junit5-jvm:${libs.versions.kotest.get()}")
  testImplementation("io.kotest.extensions:kotest-extensions-spring:${libs.versions.kotestSpring.get()}")
  testImplementation("io.mockk:mockk:${libs.versions.mockk.get()}")
  testRuntimeOnly("com.h2database:h2:${libs.versions.h2.get()}")
}

description = "app"
