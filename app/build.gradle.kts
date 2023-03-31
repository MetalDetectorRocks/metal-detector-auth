import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
  id("org.springframework.boot")
  id("de.europace.docker-publish")
}

dockerPublish {
  organisation.set("metaldetectorrocks")
  imageName.set(rootProject.name)
}

springBoot {
  mainClass.set("rocks.metaldetector.auth.MetalDetectorAuthApplicationKt")
  buildInfo().apply {
    version = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"))
  }
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

  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.security:spring-security-oauth2-authorization-server:${libs.versions.springSecurityAuthorization.get()}")

  implementation("org.flywaydb:flyway-core")
  implementation("io.micrometer:micrometer-registry-prometheus")

  runtimeOnly("com.github.loki4j:loki-logback-appender:${libs.versions.lokiLogbackAppender.get()}")
  runtimeOnly("org.postgresql:postgresql:${libs.versions.postgresql.get()}")

  developmentOnly("org.springframework.boot:spring-boot-devtools")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.kotest:kotest-framework-engine-jvm:${libs.versions.kotest.get()}")
  testImplementation("io.kotest:kotest-property-jvm:${libs.versions.kotest.get()}")
  testImplementation("io.kotest:kotest-runner-junit5-jvm:${libs.versions.kotest.get()}")
  testImplementation("io.kotest.extensions:kotest-extensions-spring:${libs.versions.kotestSpring.get()}")
  testImplementation("io.mockk:mockk:${libs.versions.mockk.get()}")
  testRuntimeOnly("com.h2database:h2")
}

description = "app"
