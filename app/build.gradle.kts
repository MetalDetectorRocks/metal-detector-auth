import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
  alias(libs.plugins.springBoot)
  alias(libs.plugins.dockerPublish)
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

  implementation(libs.bundles.springBootStarter)
  implementation(libs.micrometerRegistryPrometheus)

  runtimeOnly(libs.lokiLogbackAppender)
  runtimeOnly(libs.bundles.database)

  developmentOnly(libs.springBootDevTools)
  annotationProcessor(libs.springBootConfigurationProcessor)

  testImplementation(libs.springBootStarterTest)
  testImplementation(libs.bundles.kotest)
  testImplementation(libs.mockk)
  testRuntimeOnly(libs.h2)
}

description = "app"
