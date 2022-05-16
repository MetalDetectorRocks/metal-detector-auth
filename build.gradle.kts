val javaVersion: JavaVersion = JavaVersion.VERSION_17
val dependencyVersions: List<String> = listOf(
    "com.nimbusds:nimbus-jose-jwt:9.22",
    "org.objenesis:objenesis:3.2",
    "com.fasterxml.jackson:jackson-bom:2.13.3"
)
val dependencyGroupVersions: Map<String, String> = mapOf(
    "io.kotest" to libs.versions.kotest.get(),
    "org.springframework" to libs.versions.spring.get(),
    "org.springframework.boot" to libs.versions.springBoot.get()
)

plugins {
  val kotlinVersion = "1.6.0"
  kotlin("jvm") version kotlinVersion apply false
  kotlin("plugin.spring") version kotlinVersion apply false
  kotlin("plugin.allopen") version kotlinVersion apply false

  id("org.springframework.boot") version "2.6.7" apply false
  id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
  id("de.europace.docker-publish") version "1.3.0" apply false
}

subprojects {
  project.apply(plugin = "org.jetbrains.kotlin.jvm")
  project.apply(plugin = "org.jetbrains.kotlin.plugin.spring")
  project.apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
  project.apply(plugin = "io.spring.dependency-management")
  project.apply(plugin = "jacoco")

  repositories {
    mavenCentral()
  }

  configure<JavaPluginExtension> {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
  }

  configurations {
    all {
      resolutionStrategy {
        failOnVersionConflict()
        force(dependencyVersions)
        eachDependency {
          val forcedVersion = dependencyGroupVersions[requested.group]
          if (forcedVersion != null) {
            useVersion(forcedVersion)
          }
        }
        cacheDynamicVersionsFor(0, "seconds")
      }
    }
  }

  tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
      kotlinOptions {
        jvmTarget = javaVersion.toString()
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=compatibility")
      }
    }
    withType<JacocoReport> {
      reports {
        xml.required.set(true)
        html.required.set(false)
      }
    }
    withType<Test> {
      useJUnitPlatform()
      testLogging.showStandardStreams = true
    }
  }
}
