val javaVersion: JavaVersion = JavaVersion.VERSION_17
val dependencyVersions: List<String> = listOf()
val dependencyGroupVersions: Map<String, String> = mapOf(
    "io.kotest" to libs.versions.kotest.get(),
    "org.springframework" to libs.versions.spring.get(),
    "org.springframework.boot" to libs.versions.springBoot.get()
)

plugins {
  val kotlinVersion = "1.7.0"
  kotlin("jvm") version kotlinVersion apply false
  kotlin("plugin.spring") version kotlinVersion apply false
  kotlin("plugin.allopen") version kotlinVersion apply false

  id("org.springframework.boot") version "2.7.2" apply false
  id("io.spring.dependency-management") version "1.0.12.RELEASE" apply false
  id("de.europace.docker-publish") version "1.4.0" apply false
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
