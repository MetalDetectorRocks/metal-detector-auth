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
  implementation(libs.kotlin)

  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation(libs.springSecurityAuthorizationServer)

  implementation("org.flywaydb:flyway-core")
  implementation("io.micrometer:micrometer-registry-prometheus")

  runtimeOnly(libs.lokiLogbackAppender)
  runtimeOnly(libs.postgresql)

  developmentOnly("org.springframework.boot:spring-boot-devtools")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation(libs.bundles.kotest)
  testImplementation(libs.kotestExtensionSpring)
  testImplementation(libs.mockk)
  testRuntimeOnly("com.h2database:h2")
}

description = "app"
